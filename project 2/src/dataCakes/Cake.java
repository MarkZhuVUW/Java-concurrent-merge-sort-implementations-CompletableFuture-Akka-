package dataCakes;

@SuppressWarnings("serial")
public class Cake extends Counter{
  public Cake(Sugar s, Wheat w) {}
  volatile static int cTot=0;
  void incr() {cTot+=1;c=cTot;}
}