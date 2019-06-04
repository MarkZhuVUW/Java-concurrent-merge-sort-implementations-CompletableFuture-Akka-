
package ass1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * futures with executorServices take control of the size of the tasks, which means that by choosing the appropriate thread pool,
 * we can manually decide how many threads we want to use and how. For this assignment I am using the newCachedThreadPool because it creates new threads as needed and it reuses constructed threads when they are available.
 * This means that for the merge sort case if the threads in the pool are not enough, new threads get created unless previous threads have finished their task.
 * If we use newFixedThreadPool and give it a fixed number of threads, say 10. It indicates that there should at most be 10 threads in the pool and if all threads are in use and have not completed their task when we need to assign more tasks to the pool,
 * the program will get stuck because all threads are in use and we cannot create new threads.
 * Also if we are already doing multi-threading tasks on our computer, it should be better if we could do more sequential works because the threads should be already be busy and putting extra work on them could harm efficiency.
 * I have learned in implementing this version of merge sort that the difference between the future get method and the join method in the forkjoin library is that get method throws checked exceptions (InterruptedException, ExecutionException), whereas join method throws unchecked exceptions (RuntimeExceptions, Errors)
 * We have to explicitly catch the checked exceptions for the future.get method, whereas we do not for the join method.
 *
 */
public class MParallelSorter1 implements Sorter {
  private static final ExecutorService pool = Executors.newCachedThreadPool();
  @Override
  public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
    List<T> newList = new ArrayList<>(list);
    return mySort(newList); // sorted list.
  }

  private  <T extends Comparable<? super T>> List<T> mySort(List<T> list){
    if(list.size() < 20) return MSequentialSorter.mySort(list); //distributes the sorting of lists smaller than 20 to sequential mergesort
    List<T> firstHalf = list.subList(0, list.size()/2);
    List<T> secondHalf = list.subList(list.size()/2, list.size());
    Future<List<T>> firstHalfFJ = pool.submit(() -> mySort(firstHalf));
    mySort(secondHalf);
    MSequentialSorter.merge(get(firstHalfFJ), secondHalf, list);
    return list;
  }

  private  <T extends Comparable<? super T>> List<T> get(Future<List<T>> future){
    try {
      return future.get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new Error(e);
    } catch (ExecutionException e) {
      Throwable t = e.getCause();
      if(t instanceof RuntimeException)
        throw (RuntimeException) t;
      if(t instanceof Error)
        throw new Error();
      throw new Error("unexpected checked exception", t);
    }
  }

}
