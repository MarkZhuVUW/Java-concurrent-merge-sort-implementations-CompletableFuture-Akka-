package gui;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import datasets.DataSetLoader;
import model.Model;
import model.ModelAbstract;

public class Gui extends JFrame implements Runnable{
    private static int frameTime=1;//use a bigger or smaller number for faster/slower simulation
    private static int stepsForFrame=1000;//use a bigger or smaller number for faster/slower simulation
    //it will attempt to do 4 steps every 20 milliseconds (less if the machine is too slow)
    public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    ModelAbstract m;
    Gui(ModelAbstract m, String type){
        super(type);
        this.m=m;}
    public void run() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getRootPane().setLayout(new BorderLayout());
        JPanel p=new Canvas(m);
        getRootPane().add(p,BorderLayout.CENTER);
        pack();
        setVisible(true);
        scheduler.scheduleAtFixedRate(
                ()-> SwingUtilities.invokeLater(()->repaint()),
                500,25, TimeUnit.MILLISECONDS
        );
        SwingUtilities.invokeLater(()->repaint());
    }
    private static final class MainLoop implements Runnable {
        ModelAbstract m;MainLoop(ModelAbstract m){this.m=m;}
        public void run() {
            try{
                while(true){
                    long ut=System.currentTimeMillis();
                    for(int i=0;i<stepsForFrame;i++) {m.step();}
                    ut=System.currentTimeMillis() - ut;//used time
                    //System.out.println("Particles: "+ m.p.size()+ " time:" + ut);//if you want to have an idea of the time consumption
                    long sleepTime=frameTime- ut;
                    if(sleepTime>1){
                        Thread.sleep(sleepTime);
                    }
                }//if the step was short enough, it wait to make it at least frameTime long.
            }
            catch(Throwable t){//not a perfect solution, but
                t.printStackTrace();//makes sure you see the error and the program dies.
                System.exit(0);//the "right" solution is much more involved
            }//and would require storing and passing the exception between different objects.
        }
    }
    public static void main(String[] args) {
//        ModelAbstract m=DataSetLoader.getRegularGrid(100, 800, 40, false);//Try those configurations
//        ModelAbstract m=DataSetLoader.getRandomRotatingGrid(100, 800, 40, false);
        ModelAbstract m=DataSetLoader.getRandomSet(100, 800, 10000, false);
//        ModelAbstract m=DataSetLoader.getRandomSet(100, 800, 100, false);
//        ModelAbstract m=DataSetLoader.getRandomGrid(100, 800, 30, false);

//        ModelAbstract mPara=DataSetLoader.getRegularGrid(100, 800, 40, true);//Try those configurations
//        ModelAbstract mPara=DataSetLoader.getRandomRotatingGrid(100, 800, 40, true);
        ModelAbstract mPara=DataSetLoader.getRandomSet(100, 800, 10000, true);
//        ModelAbstract mPara=DataSetLoader.getRandomSet(100, 800, 100, true);
//        ModelAbstract mPara=DataSetLoader.getRandomGrid(100, 800, 30, true);
        scheduler.schedule(new MainLoop(m), 500, TimeUnit.MILLISECONDS);
        SwingUtilities.invokeLater(new Gui(m, "sequential"));
        scheduler.schedule(new MainLoop(mPara), 500, TimeUnit.MILLISECONDS);
        SwingUtilities.invokeLater(new Gui(mPara, "parallel"));
    }
}