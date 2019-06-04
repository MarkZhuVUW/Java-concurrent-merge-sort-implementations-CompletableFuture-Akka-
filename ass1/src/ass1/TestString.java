package ass1;

import org.junit.jupiter.api.Test;
import java.util.Random;

public class TestString {
    public  final String[][] dataset={
            {new String("qwirgvpeiurwpiq"),new String("asdaopwpoqer"),new String("aaaaaaa"),new String("vhewpofhbwpe"),new String("opquhbnbewuhrqu")},
            {new String("vmbsfjieiwepbp"),new String("bbbbbbbbbb"),new String("ebwhrtrthwrth"),new String("asdfasdfasfd"),new String("fmfjmfjmfg")},
            {new String("wewetbwetbwetb"),new String("uymtuimtuymty"),new String("tmtumeyhehtyh"),new String("-qwoirytoisdfjbfv"),new String("eopubhbqerbw")},
            {new String("zlxjvhoaiurhgoa"),new String("sifhbpeorjbwp"),new String("qpougrpoewruuuuuu"),new String("-asopfjvhspofnnnn"),new String("fouvhqopfu")},
            {},
            manyRandomUsingDiffSeed1(10000),
            manyRandomUsingDiffSeed2(10000),
            manyRandom(10000)
    };
     private String[] manyRandom(int size) {
        Random r=new Random(0);
        String[] result=new String[size];
        for(int i=0;i<size;i++){
            result[i]=new String(randomString(r));
        }
        return result;
    }

    /**
     * I know it is stupid but the only way I can think of.
     * @param r
     * @return
     */
    private  String randomString(Random r){
        switch(r.nextInt(25)){
            case 0:return "a";
            case 1:return "b";
            case 2:return "c";
            case 3:return "d";
            case 4:return "e";
            case 5:return "f";
            case 6:return "g";
            case 7:return "h";
            case 8:return "i";
            case 9:return "j";
            case 10:return "k";
            case 11:return "l";
            case 12:return "m";
            case 13:return "n";
            case 14:return "o";
            case 15:return "p";
            case 16:return "q";
            case 17:return "r";
            case 18:return "s";
            case 19:return "t";
            case 20:return "u";
            case 21:return "v";
            case 22:return "w";
            case 23:return "x";
            case 24:return "y";
            case 25:return "z";
        }
        return null;
    }
     private String[] manyRandomUsingDiffSeed1(int size) {
        Random r = new Random(System.currentTimeMillis());
        String[] result=new String[size];
        for(int i=0;i<size;i++){result[i]=new String(
                randomString(r));
        }
        return result;
    }
     private String[] manyRandomUsingDiffSeed2(int size) {
        String[] result=new String[size];
        Random r = new Random(System.currentTimeMillis() * 5);
        for(int i=0;i<size;i++){result[i]=new String(randomString(r));}
        return result;
    }

    @Test
    public void testISequentialSorter() {
        Sorter s=new ISequentialSorter();
        for(String[]l:dataset){TestHelper.testData(l,s);}
    }
    @Test
    public void testMSequentialSorter() {
        Sorter s=new MSequentialSorter();
        for(String[]l:dataset){TestHelper.testData(l,s);}
    }
    @Test
    public void testMParallelSorter1() {
        Sorter s=new MParallelSorter1();
        for(String[]l:dataset){TestHelper.testData(l,s);
        }
    }
    @Test
    public void testMParallelSorter2() {
        Sorter s=new MParallelSorter2();
        for(String[]l:dataset){TestHelper.testData(l,s);}
    }

}
