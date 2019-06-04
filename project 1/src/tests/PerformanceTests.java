package tests;

import datasets.DataSetLoader;
import model.ModelAbstract;
import org.junit.jupiter.api.Test;

import static java.lang.System.currentTimeMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PerformanceTests {
    private int steps = 600;

    public void test01(){
        ModelAbstract parallel = DataSetLoader.getRandomGrid(100, 800, 100, true);
        ModelAbstract sequential = DataSetLoader.getRandomGrid(100, 800, 100, false);
        long start = currentTimeMillis(), end;
        for (int i = 0; i < steps; i++)
            parallel.step();
        end = currentTimeMillis();
        System.out.println("parallel model Time taken: " + (end - start));
        start= currentTimeMillis();
        for (int i = 0; i < steps; i++)
            sequential.step();
        end = currentTimeMillis();
        System.out.println("sequential model Time taken: " + (end - start) + "\n\n\n");
    }

    public void test02(){
        ModelAbstract parallel = DataSetLoader.getRandomRotatingGrid(100, 800, 50, true);
        ModelAbstract sequential = DataSetLoader.getRandomRotatingGrid(100, 800, 50, false);
        long start = currentTimeMillis(), end;
        for (int i = 0; i < steps; i++)
            parallel.step();
        end = currentTimeMillis();
        System.out.println("parallel model Time taken: " + (end - start));
        start= currentTimeMillis();
        for (int i = 0; i < steps; i++)
            sequential.step();
        end = currentTimeMillis();
        System.out.println("sequential model Time taken: " + (end - start) + "\n\n\n");
    }

    public void test03(){
        ModelAbstract parallel = DataSetLoader.getRandomRotatingGrid(100, 800, 40, true);
        ModelAbstract sequential = DataSetLoader.getRandomRotatingGrid(100, 800, 40, false);
        long start = currentTimeMillis(), end;
        for (int i = 0; i < steps; i++)
            parallel.step();
        end = currentTimeMillis();
        System.out.println("parallel model Time taken: " + (end - start));
        start= currentTimeMillis();
        for (int i = 0; i < steps; i++)
            sequential.step();
        end = currentTimeMillis();
        System.out.println("sequential model Time taken: " + (end - start) + "\n\n\n");
    }

    public void test04(){
        ModelAbstract parallel = DataSetLoader.getRandomRotatingGrid(100, 800, 60, true);
        ModelAbstract sequential = DataSetLoader.getRandomRotatingGrid(100, 800, 60, false);
        long start = currentTimeMillis(), end;
        for (int i = 0; i < steps; i++)
            parallel.step();
        end = currentTimeMillis();
        System.out.println("parallel model Time taken: " + (end - start));
        start= currentTimeMillis();
        for (int i = 0; i < steps; i++)
            sequential.step();
        end = currentTimeMillis();
        System.out.println("sequential model Time taken: " + (end - start) + "\n\n\n");
    }

    public void test05(){
        ModelAbstract parallel = DataSetLoader.getRandomGrid(100, 800, 30, true);
        ModelAbstract sequential = DataSetLoader.getRandomGrid(100, 800, 30, false);
        long start = currentTimeMillis(), end;
        for (int i = 0; i < steps; i++)
            parallel.step();
        end = currentTimeMillis();
        System.out.println("parallel model Time taken: " + (end - start));
        start= currentTimeMillis();
        for (int i = 0; i < steps; i++)
            sequential.step();
        end = currentTimeMillis();
        System.out.println("sequential model Time taken: " + (end - start) + "\n\n\n");
    }

    public void test06(){
        ModelAbstract parallel = DataSetLoader.getRandomRotatingGrid(100, 800, 40, true);
        ModelAbstract sequential = DataSetLoader.getRandomRotatingGrid(100, 800, 40, false);
        long start = currentTimeMillis(), end;
        for (int i = 0; i < steps; i++)
            parallel.step();
        end = currentTimeMillis();
        System.out.println("parallel model Time taken: " + (end - start));
        start= currentTimeMillis();
        for (int i = 0; i < steps; i++)
            sequential.step();
        end = currentTimeMillis();
        System.out.println("sequential model Time taken: " + (end - start) + "\n\n\n");
    }

    public void test07(){
        ModelAbstract parallel = DataSetLoader.getRandomSet(100, 800, 500, true);
        ModelAbstract sequential = DataSetLoader.getRandomSet(100, 800, 500, false);
        long start = currentTimeMillis(), end;
        for (int i = 0; i < steps; i++)
            parallel.step();
        end = currentTimeMillis();
        System.out.println("parallel model Time taken: " + (end - start));
        start= currentTimeMillis();
        for (int i = 0; i < steps; i++)
            sequential.step();
        end = currentTimeMillis();
        System.out.println("sequential model Time taken: " + (end - start) + "\n\n\n");
    }

    public void test08(){
        ModelAbstract parallel = DataSetLoader.getRandomSet(100, 800, 100, true);
        ModelAbstract sequential = DataSetLoader.getRandomSet(100, 800, 100, false);
        long start = currentTimeMillis(), end;
        for (int i = 0; i < steps; i++)
            parallel.step();
        end = currentTimeMillis();
        System.out.println("parallel model Time taken: " + (end - start));
        start= currentTimeMillis();
        for (int i = 0; i < steps; i++)
            sequential.step();
        end = currentTimeMillis();
        System.out.println("sequential model Time taken: " + (end - start) + "\n\n\n");
    }

    public void test09(){
        ModelAbstract parallel = DataSetLoader.getRandomSet(100, 800, 1000, true);
        ModelAbstract sequential = DataSetLoader.getRandomSet(100, 800, 1000, false);
        long start = currentTimeMillis(), end;
        for (int i = 0; i < steps; i++)
            parallel.step();
        end = currentTimeMillis();
        System.out.println("parallel model Time taken: " + (end - start) );
        start= currentTimeMillis();
        for (int i = 0; i < steps; i++)
            sequential.step();
        end = currentTimeMillis();
        System.out.println("sequential model Time taken: " + (end - start) + "\n\n\n");
    }

    public void test10(){
        ModelAbstract parallel = DataSetLoader.getRegularGrid(100, 800, 60, true);
        ModelAbstract sequential = DataSetLoader.getRegularGrid(100, 800, 60, false);
        long start = currentTimeMillis(), end;
        for (int i = 0; i < steps; i++)
            parallel.step();
        end = currentTimeMillis();
        System.out.println("parallel model Time taken: " + (end - start));
        start= currentTimeMillis();
        for (int i = 0; i < steps; i++)
            sequential.step();
        end = currentTimeMillis();
        System.out.println("sequential model Time taken: " + (end - start) + "\n\n\n");
    }
    public static void main(String[] args){
        PerformanceTests p = new PerformanceTests();
        p.test01();
        p.test02();
        p.test03();
        p.test04();
        p.test05();
        p.test06();
        p.test07();
        p.test08();
        p.test09();
        p.test10();
    }
}
