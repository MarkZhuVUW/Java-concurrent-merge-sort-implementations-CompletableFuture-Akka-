
package ass1;

import java.util.ArrayList;
import java.util.List;

/**
 * The sequential sorter should generally be slower if we are sorting large datasets.
 * This is because the algorithm itself recursively splits a collection into very very tiny blocks (size 1 blocks) which involves a plethora of costly method calls.
 * We could speed up the process by multi-threading the work load so that instead of running all the method calls on one thread, each thread does a proportion of methods calls.
 * However parallelising merge sort is not always going to speed up the process. This is because creating/starting a thread is costly. Suppose if the cost of creating a single thread is 100 seconds and the thread is only assigned 1 millisecond of workload, of course it would not speed up the sorting.
 * What I learned from implementing the sequential merge sort this way is that it is actually faster than the other 2 parrallelized merge sorts when dealing with smaller datasets.
 */
public class MSequentialSorter implements Sorter {

  @Override
  public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
    List<T> newList = new ArrayList<>(list);
    return mySort(newList);
  }

  protected static <T extends Comparable<? super T>> List<T> mySort(List<T> list){
    if(list.size() <= 1) return list; // list has been split into 1-element pieces.
    List<T> firstHalf = list.subList(0, list.size()/2);
    List<T> secondHalf = list.subList(list.size()/2, list.size());
    mySort(firstHalf);
    mySort(secondHalf);
    merge(firstHalf, secondHalf, list);

    return list; // sorted list.
  }
  /**
   * merges and sorts two pieces of list.
   * @param firstHalf the first half of the list.
   * @param secondHalf the second half of the list.
   * @param sortedList the sorted list
   * @param <T> generic type T
   */
  protected static <T extends Comparable<? super T>> void merge(List<T> firstHalf, List<T> secondHalf, List<T> sortedList){
    int index1 = 0;
    int index2 = 0;
    int sortedIndex = 0;
    while(index1 < firstHalf.size() && index2 < secondHalf.size())
      if(firstHalf.get(index1).compareTo(secondHalf.get(index2)) < 0)
        sortedList.set(sortedIndex++, firstHalf.get(index1++));
      else
        sortedList.set(sortedIndex++ ,secondHalf.get(index2++));
  }


}
