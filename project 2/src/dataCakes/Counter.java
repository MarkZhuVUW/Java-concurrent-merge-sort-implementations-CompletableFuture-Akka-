package dataCakes;

import java.io.Serializable;

@SuppressWarnings("serial")
abstract class Counter implements Serializable{
  volatile int c;
  Counter(){
    synchronized(this.getClass()) {
      this.incr();
      //System.out.println(this.getClass().getName()+" number "+c+" produced");
    }
  }
  abstract void incr();
}