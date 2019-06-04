/**
 * The average time complexity of insertion sort is O(n^2). Worst case O(n^2) happens when a collection is sorted in reverse order. Best case O(n) happens when a collection is almost sorted.
 * The space complexity of insertion sort is n.
 * In comparison with merge sort and parallelized merge sorts,
 * insertion sort works better on smaller data sets because unlike merge sort insertion sort does not do much object creating and recursive stack creating.
 */
package ass1;

import java.util.ArrayList;
import java.util.List;

/*
 * A simple sequential implementation of insertion sort
 */
public class ISequentialSorter implements Sorter{

  @Override
  public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
    List<T>result=new ArrayList<>();
    for(T l:list){insert(result,l);}
    return result;
  }
  <T extends Comparable<? super T>> void insert(List<T> list, T elem){
    for(int i=0;i<list.size();i++){
      if(list.get(i).compareTo(elem)<0){continue;}
      list.add(i,elem);
      return;
    }
    list.add(elem);
  }

}
