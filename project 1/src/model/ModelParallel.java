package model;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The goal is to parallelize parts of step method, namely the mergeParticles method and interact method.
 */
public class ModelParallel extends ModelAbstract {
    private boolean isShutDown;
    private ExecutorService pool = Executors.newCachedThreadPool();
    //    declare the set as field just to help increase performance.
    //creating hashSet object in each step is expensive.

    @Override
    public void step() {
        if(p.size() > 50)
            doParallel();
        else
            doSequential();
        updateGraphicalRepresentation();
    }
    private void doParallel(){
        //do p.interact in parallelStream
        p.parallelStream().forEach(p ->p.interact(this));
        mergeParticlesParallel();//parallelize mergeParticles method.
        //do p.move in parallelStream
        p.parallelStream().forEach(p ->p.move(this));

    }

    private void doSequential(){
        if(!isShutDown) {
            pool.shutdownNow(); // shut down the pool as it is consuming unnecessary resources
            isShutDown = true;
        }
        for(Particle p:this.p){p.interact(this);}
        mergeParticles(); // bug mergeParticles() should not be called here
        for(Particle p:this.p){p.move(this);}
    }
    private void mergeParticlesParallel(){

        Stack<Particle> deadPs=new Stack<>();
        List<Set<Particle>> chunksToMerge = new ArrayList<>();
        for(Particle p:this.p){ if(!p.impacting.isEmpty()){deadPs.add(p);}}
        this.p.removeAll(deadPs);
        while(!deadPs.isEmpty()){
            Particle current=deadPs.pop();
            Set<Particle> ps=getSingleChunck(current);
            chunksToMerge.add(ps);
            deadPs.removeAll(ps);
        }
        //in this way we can decouple the merging and adding of particles with the while loop and parallelize this separately.
        List<Future<Particle>> futureTasks = new ArrayList<>();
        for (Set<Particle> chunk : chunksToMerge)
            futureTasks.add(pool.submit(() -> mergeParticles(chunk)));
        for(Future<Particle> future : futureTasks)
            this.p.add(get(future));
    }

    private Particle get(Future<Particle> future){
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
