package akkaCakes;

import java.io.Serializable;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.pattern.Patterns;
import akkaUtils.AkkaConfig;
import dataCakes.Cake;
import dataCakes.Gift;
import dataCakes.Sugar;
import dataCakes.Wheat;

@SuppressWarnings("serial")
final class GiftRequest implements Serializable{}
@SuppressWarnings("serial")
final class MakeOne implements Serializable{}
@SuppressWarnings("serial")
final class GiveOne implements Serializable{}
class Alice extends Producer<Wheat>{

    @Override
    public CompletableFuture<Wheat> make() {
        return CompletableFuture.supplyAsync(Wheat::new);
    }

    @Override
    public Class<Wheat> getClassObj() {
        return Wheat.class;
    }
}
class Bob extends Producer<Sugar>{
    @Override
    public CompletableFuture<Sugar> make(){
        return  CompletableFuture.supplyAsync(Sugar::new);
    }

    @Override
    public Class<Sugar> getClassObj() {
        return Sugar.class;
    }
}
class Charles extends Producer<Cake>{
    private ActorRef alice, bob1, bob2, bob3, bob4;
    private int bobChoice;
    Charles(ActorRef alice, ActorRef bob1, ActorRef bob2, ActorRef bob3, ActorRef bob4) {
        this.alice = alice;
        this.bob1 = bob1;
        this.bob2 = bob2;
        this.bob3 = bob3;
        this.bob4 = bob4;
    }

    @Override
    public CompletableFuture<Cake> make() {
        ActorRef bobTheChosenOne = bobChooser();
        CompletableFuture<Object> fWheat = Patterns.ask(alice, new GiveOne(), Duration.ofMillis(10000)).toCompletableFuture();
        CompletableFuture<Object> fSugar = Patterns.ask(bobTheChosenOne, new GiveOne(), Duration.ofMillis(10000)).toCompletableFuture();
//        CompletableFuture<Object> fSugar = Patterns.ask(bob1, new GiveOne(), Duration.ofMillis(10000)).toCompletableFuture();

        return CompletableFuture.allOf(fWheat, fSugar).thenApplyAsync(v -> {
            Wheat wheat = (Wheat) fWheat.join();
            Sugar sugar = (Sugar) fSugar.join();
            return new Cake(sugar, wheat);
        });
    }

    private ActorRef bobChooser(){
        if(bobChoice >= 4)
            bobChoice = 0;
        switch(bobChoice++){
            case 0: return bob1;
            case 1: return bob2;
            case 2: return bob3;
            case 3: return bob4;
        }
        return null;
    }


    @Override
    public Class<Cake> getClassObj() {
        return Cake.class;
    }
}

class Tim extends AbstractActor{
    private int hunger;
    private ActorRef charles;
    public Tim(int hunger, ActorRef charles) {
        this.hunger=hunger;
        this.charles = charles;
    }
    private boolean running=true;
    private ActorRef originalSender=null;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GiftRequest.class,()-> originalSender==null, gr-> {
                    originalSender=sender();
                    charles.tell(new GiveOne(), self());
                })
                .match(Cake.class,()->running,c->{
                    hunger-=1;
                    System.out.println("YUMMY but I'm still hungry "+hunger);
                    if(hunger>0) {
                        charles.tell(new GiveOne(), self());
                        return;
                    }
                    running=false;
                    originalSender.tell(new Gift(),self());
                })
                .build();}
}

public class Cakes{
    public static void main(String[] args){
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
        Gift g=computeGift(1000);
        assert g!=null;
        System.out.println(
                "\n\n-----------------------------\n\n"+
                        g+
                        "\n\n-----------------------------\n\n");
    }
    public static Gift computeGift(int hunger){
        ActorSystem s=AkkaConfig.newSystem("Cakes", 2501,AkkaConfig.makeMap(
        "bob1","130.195.6.192",
        "bob2","130.195.6.176",
        "bob3","130.195.6.135",
        "bob4","130.195.6.135"

        ));
        ActorRef alice=//makes wheat
                s.actorOf(Props.create(Alice.class, Alice::new),"Alice");
        ActorRef bob1=//makes sugar
                s.actorOf(Props.create(Bob.class, Bob::new),"Bob1");
        ActorRef bob2=//makes more sugar
                s.actorOf(Props.create(Bob.class, Bob::new),"Bob2");
        ActorRef bob3=//makes much more sugar
                s.actorOf(Props.create(Bob.class, Bob::new),"Bob3");
        ActorRef bob4=//makes much much more sugar
                s.actorOf(Props.create(Bob.class, Bob::new),"Bob4");
        ActorRef charles=// makes cakes with wheat and sugar
                s.actorOf(Props.create(Charles.class,()->new Charles(alice, bob1, bob2, bob3, bob4)),"Charles");
        ActorRef tim=//tim wants to eat 1000 cakes
                s.actorOf(Props.create(Tim.class,()->new Tim(hunger, charles)),"Tim");

        long startTime = System.currentTimeMillis();
        CompletableFuture<Object> gift = Patterns.ask(tim,new GiftRequest(), Duration.ofMillis(2000000000)).toCompletableFuture();
        try{
            Gift g = (Gift)gift.join();
            long timeTaken = System.currentTimeMillis() - startTime;
            System.out.println("||||||||total time taken to get the gift: " + timeTaken + "ms" + "||||||||");
            return g;}
        finally{
            alice.tell(PoisonPill.getInstance(),ActorRef.noSender());
            bob1.tell(PoisonPill.getInstance(),ActorRef.noSender());
            bob2.tell(PoisonPill.getInstance(),ActorRef.noSender());
            bob3.tell(PoisonPill.getInstance(),ActorRef.noSender());
            bob4.tell(PoisonPill.getInstance(),ActorRef.noSender());
            charles.tell(PoisonPill.getInstance(),ActorRef.noSender());
            tim.tell(PoisonPill.getInstance(),ActorRef.noSender());
            s.terminate();
        }
    }
}