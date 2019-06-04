package akkaUtils;


import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ChildRestartStats;
import akka.actor.SupervisorStrategy;
import akka.actor.Terminated;
import scala.PartialFunction;
import scala.collection.Iterable;

class TerminatorSupervisor implements akka.actor.SupervisorStrategyConfigurator{
  @Override public SupervisorStrategy create() {
    SupervisorStrategy s = SupervisorStrategy.stoppingStrategy();
    return new SupervisorStrategy() {
      @Override public PartialFunction<Throwable, Directive> decider() {
        return s.decider();}
      @Override public void handleChildTerminated(ActorContext context, ActorRef child, Iterable<ActorRef> children) {
        s.handleChildTerminated(context, child, children);}
      @Override public void processFailure(ActorContext context, boolean restart, ActorRef child, Throwable cause,
          ChildRestartStats stats, Iterable<ChildRestartStats> children) {
        s.processFailure(context, restart, child, cause, stats, children);
        context.system().terminate();
      }};
    }  
}

public class AkkaConfig {
  public static Map<String,String> makeMap(String ... nameIp){
    assert nameIp.length%2==0;
    Map<String,String> res=new HashMap<>();
    for(int i=0;i<nameIp.length;i+=2) {res.put(nameIp[i],nameIp[i+1]);}
    return res;
  }
  public static ActorSystem newSystem(String name,int port,Map<String,String> mapPathIp) {
    List<String> ips = AkkaConfig.guessMyIp();
    if(ips.size()!=1) {
      System.err.println("Unable to detect ip between the following options:\n"+ips);
      System.exit(0);
      }
    System.out.println("Chosen IP is\n------------------------------\n    "
        +ips.get(0)+"\n-------------------------" +
            "-----\n");
    Config config = ConfigFactory.parseString(
        "akka.actor.guardian-supervisor-strategy = " + TerminatorSupervisor.class.getCanonicalName()
        ).withFallback(ConfigFactory.parseString(        
        "akka.actor.provider = remote"
        )).withFallback(ConfigFactory.parseString(
        "akka.remote.enabled-transports = [\"akka.remote.netty.tcp\"]"
        )).withFallback(ConfigFactory.parseString(
        "akka.remote.netty.tcp.hostname = \""+ips.get(0)+"\""
        )).withFallback(ConfigFactory.parseString(
        "akka.remote.netty.tcp.port = "+port
        ));
    for(Entry<String, String> e:mapPathIp.entrySet()) {
      Config c = ConfigFactory.parseString(
        "akka.actor.deployment.\"/"+e.getKey()+"\".remote = \"akka.tcp://OpenAkka@"+e.getValue()+":2500\"");
      config=config.withFallback(c);
      System.out.println(e);
      }
    config=config.withFallback(ConfigFactory.load());
    return ActorSystem.create(name,config);
    }
  public static List<String> guessMyIp() {
    List<String> res=new ArrayList<>();
    try {
      for(NetworkInterface n:Collections.list(NetworkInterface.getNetworkInterfaces())) {
        // filters out 127.0.0.1 and inactive interfaces
        if (n.isLoopback() || !n.isUp()) {continue;}
        for(InetAddress a: Collections.list(n.getInetAddresses())) {
          try {
            String ip=a.getHostAddress();
            if(ip.chars().filter(i->i=='.').count()!=3) {continue;}
            if(a.isReachable(1000)) {res.add(a.getHostAddress());}
          } catch (IOException e) {throw new Error(e);}
        }
      }
    }catch (SocketException e) {throw new Error(e);}
    return res;
  }
  public static void waitTermination(ActorSystem s) throws InterruptedException{
    CompletableFuture<Terminated> cf = s.getWhenTerminated().toCompletableFuture();
    try {cf.get();} catch (ExecutionException e) {throw new Error(e);}
    }
  public static void keybordClose(ActorSystem s) throws InterruptedException{
    CompletableFuture<Terminated> cf = s.getWhenTerminated().toCompletableFuture();
    System.out.println(">>> Press ENTER to exit "+s.name()+" <<<");
    try{while(!cf.isDone()) {
      if(System.in.available()>=1) {s.terminate();return;}
      Thread.sleep(200);
      }}
    catch (IOException ioe) {s.terminate();}
      };  
  }
