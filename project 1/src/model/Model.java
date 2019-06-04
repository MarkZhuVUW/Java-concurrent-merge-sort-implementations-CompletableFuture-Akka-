package model;


import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Model extends ModelAbstract{

    public void step() {
        for(Particle p:this.p){p.interact(this);}
        mergeParticles(); // bug mergeParticles() should not be called here
        for(Particle p:this.p){p.move(this);}
        updateGraphicalRepresentation();

    }

}
