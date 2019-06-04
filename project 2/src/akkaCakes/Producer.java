package akkaCakes;

import akka.actor.AbstractActor;
import akka.pattern.Patterns;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

public abstract class Producer <T> extends AbstractActor {
    Queue<T> products = new LinkedList<>();
    final int maxSize = 20;
    boolean running ;
    public abstract CompletableFuture<T> make() throws InterruptedException;
    public abstract Class<T> getClassObj();
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(getClassObj(), r -> products.add(r))
                .match(MakeOne.class, () -> running, r -> {
                    //System.out.println(products.size());
                    if(products.size() <= maxSize){
                        CompletableFuture<T> future = make();
                        Patterns.pipe(future, context().dispatcher()).to(self());
                        future.thenAcceptAsync( unused -> self().tell(new MakeOne(), self()));
                        return;
                    }
                    running = false;
                }).match(GiveOne.class, r -> {
                    if(products.isEmpty())
                        Patterns.pipe(make(), context().dispatcher()).to(sender());
                    else
                        sender().tell(products.remove(), self());
                    if(!running) {
                        running = true;
                        self().tell(new MakeOne(), self());
                    }
                })
                .build();}
}
