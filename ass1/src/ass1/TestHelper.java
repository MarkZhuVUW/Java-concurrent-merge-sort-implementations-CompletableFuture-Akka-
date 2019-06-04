package ass1;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestHelper {
  public static <T extends Comparable<? super T>> boolean verify(List<T> list){
    for(int i=1;i<list.size();i++){
      if(list.get(i-1).compareTo(list.get(i))>0){
          return false;
      }
    }
    return true;
  }
    public static <T extends Comparable<? super T>> void
    testData(T[]data,Sorter s){
        List<T> original=new ArrayList<>(Arrays.asList(data));
        List<T> result=s.sort(original);
        assertTrue(verify(result));
        assertEquals(result.size(),data.length);
        assertEquals(Arrays.asList(data),original);//new check!
    }

}
