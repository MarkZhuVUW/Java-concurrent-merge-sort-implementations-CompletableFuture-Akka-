package tests;

import datasets.DataSetLoader;
import model.Model;
import model.ModelAbstract;
import model.ModelParallel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Tests {
    private int steps = 200;
    @Test
    public void test01(){
        ModelAbstract parallel = DataSetLoader.getRandomGrid(100, 800, 100, true);
        ModelAbstract sequential = DataSetLoader.getRandomGrid(100, 800, 100, false);
        for (int i = 0; i < steps; i++) {
            assertEquals(parallel.p.size(), sequential.p.size());
            parallel.step();
            sequential.step();
            for (int j = 0; j < parallel.p.size(); j++)
                assertEquals(parallel.p.get(j), sequential.p.get(j));
        }
    }

    @Test
    public void test02(){
        ModelAbstract parallel = DataSetLoader.getRandomRotatingGrid(100, 800, 50, true);
        ModelAbstract sequential = DataSetLoader.getRandomRotatingGrid(100, 800, 50, false);
        for (int i = 0; i < steps; i++) {
            parallel.step();
            sequential.step();
            assertEquals(parallel.p.size(), sequential.p.size());
            for (int j = 0; j < parallel.p.size(); j++)
                assertEquals(parallel.p.get(j), sequential.p.get(j));
        }
    }

    @Test
    public void test03(){
        ModelAbstract parallel = DataSetLoader.getRandomRotatingGrid(100, 800, 40, true);
        ModelAbstract sequential = DataSetLoader.getRandomRotatingGrid(100, 800, 40, false);
        for (int i = 0; i < steps; i++) {
            assertEquals(parallel.p.size(), sequential.p.size());
            parallel.step();
            sequential.step();
            for (int j = 0; j < parallel.p.size(); j++)
                assertEquals(parallel.p.get(j), sequential.p.get(j));
        }
    }

    @Test
    public void test04(){
        ModelAbstract parallel = DataSetLoader.getRandomRotatingGrid(100, 800, 60, true);
        ModelAbstract sequential = DataSetLoader.getRandomRotatingGrid(100, 800, 60, false);
        for (int i = 0; i < steps; i++) {
            assertEquals(parallel.p.size(), sequential.p.size());
            parallel.step();
            sequential.step();

            for (int j = 0; j < parallel.p.size(); j++)
                assertEquals(parallel.p.get(j), sequential.p.get(j));
        }
    }

    @Test
    public void test05(){
        ModelAbstract parallel = DataSetLoader.getRandomGrid(100, 800, 30, true);
        ModelAbstract sequential = DataSetLoader.getRandomGrid(100, 800, 30, false);
        for (int i = 0; i < steps; i++) {

            parallel.step();
            sequential.step();
            assertEquals(parallel.p.size(), sequential.p.size());

            for (int j = 0; j < parallel.p.size(); j++)
                assertEquals(parallel.p.get(j), sequential.p.get(j));
        }
    }

    @Test
    public void test06(){
        ModelAbstract parallel = DataSetLoader.getRandomRotatingGrid(100, 800, 40, true);
        ModelAbstract sequential = DataSetLoader.getRandomRotatingGrid(100, 800, 40, false);
        for (int i = 0; i < steps; i++) {

            parallel.step();
            sequential.step();
            assertEquals(parallel.p.size(), sequential.p.size());
            for (int j = 0; j < parallel.p.size(); j++)
                assertEquals(parallel.p.get(j), sequential.p.get(j));
        }
    }

    @Test
    public void test07(){
        ModelAbstract parallel = DataSetLoader.getRandomSet(100, 800, 500, true);
        ModelAbstract sequential = DataSetLoader.getRandomSet(100, 800, 500, false);
        for (int i = 0; i < steps; i++) {

            parallel.step();
            sequential.step();
            assertEquals(parallel.p.size(), sequential.p.size());
            for (int j = 0; j < parallel.p.size(); j++)
                assertEquals(parallel.p.get(j), sequential.p.get(j));
//            for (int j = 0; j < parallel.p.size(); j++){
//                assertTrue(sequential.p.contains(parallel.p.get(i)));
//            }
        }
    }

    @Test
    public void test08(){
        ModelAbstract parallel = DataSetLoader.getRandomSet(100, 800, 100, true);
        ModelAbstract sequential = DataSetLoader.getRandomSet(100, 800, 100, false);
        for (int i = 0; i < steps; i++) {
            assertEquals(parallel.p.size(), sequential.p.size());
            parallel.step();
            sequential.step();
            assertEquals(parallel.p.size(), sequential.p.size());
            for (int j = 0; j < parallel.p.size(); j++)
                assertEquals(parallel.p.get(j), sequential.p.get(j));
        }
    }

    @Test
    public void test09(){
        ModelAbstract parallel = DataSetLoader.getRandomSet(100, 800, 1000, true);
        ModelAbstract sequential = DataSetLoader.getRandomSet(100, 800, 1000, false);
        for (int i = 0; i < steps; i++) {
            parallel.step();
            sequential.step();
            assertEquals(parallel.p.size(), sequential.p.size());
            for (int j = 0; j < parallel.p.size(); j++)
                assertEquals(parallel.p.get(j), sequential.p.get(j));
//            for (int j = 0; j < parallel.p.size(); j++){
//                assertTrue(sequential.p.contains(parallel.p.get(i)));
//            }
        }
    }

    @Test
    public void test10(){
        ModelAbstract parallel = DataSetLoader.getRegularGrid(100, 800, 60, true);
        ModelAbstract sequential = DataSetLoader.getRegularGrid(100, 800, 60, false);
        for (int i = 0; i < steps; i++) {
            parallel.step();
            sequential.step();
            assertEquals(parallel.p.size(), sequential.p.size());
            for (int j = 0; j < parallel.p.size(); j++)
                assertEquals(parallel.p.get(j), sequential.p.get(j));
        }
    }
}
