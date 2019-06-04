
package ass1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 *
 *  * forkjoinpool is a form of ExecutorService that uses working stealing algorithm.
 *  * That is to say that each worker owns a task queue.
 *  * If a worker runs out of task to do in its own queue, it tries to steal task from other workers.
 *  * The benefits of this is that:
 *  * 1. Solves the bottleneck problem. Unlike a Executor threadpool, where all threads try to get their next task in a single common task queue, each of the workers in th forkjoin pool tries to steal task from other worker's queue.
 *  * In this way the probability of multiple threads accessing the same queue for tasks could be greatly reduced.
 *  * 2. better locality. Since the workers do the tasks it spawns first, for example in the forkjoinpool merge sort below, if in a method call a thread is started where the list size is 100, all tasks spawned later on for this list should be put into the queue of this thread.
 *  * So for the merge sort example below a list of size 100 gets splitted into 50, 25.... and all the tasks spawned are put into the task queue of the same thread so that the thread will prioritize doing these tasks.
 * I have learned from implementing this that preserving the original list so that we could do a non-in-place algorithm is important. This is because if a problem is complicated enough, for example the parallel merge sort algorithm where we have to put together parallelism and recursion and object mutations,
 * it is always better to simplify the method as much as possible.
 * One way to simplify the method is to not do in place algorithm. It could be really hard logically to think about implementing in-place algorithms because we could easily lose track of what is originally in the list and get the result wrong.
 */
public class MParallelSorter2 implements Sorter {
  private static final ForkJoinPool mainPool = new ForkJoinPool();
  @Override
  public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
    List<T> newList = new ArrayList<>(list);
    mainPool.invoke(new ForkJoinMergeSort<>(newList));
    return newList;
  }

  private class ForkJoinMergeSort<T extends Comparable<? super T>> extends RecursiveTask<List<T>> {
    private static final int threshold = 20;
    private List<T> list;

    private ForkJoinMergeSort(List<T> list) {
      this.list = list;
    }

    @Override
    protected List<T> compute() {
      if(list.size() < threshold) return MSequentialSorter.mySort(list); //distributes the sorting of lists smaller than 20 to sequential mergesort
//      if(list.size() <= 1) return list; // list has been split into 1-element pieces.
      List<T> firstHalf = list.subList(0, list.size()/2);
      List<T> secondHalf = list.subList(list.size()/2, list.size());
      ForkJoinMergeSort<T> firstHalfFJ = new ForkJoinMergeSort<>(firstHalf);
      ForkJoinMergeSort<T> secondHalfFJ = new ForkJoinMergeSort<>(secondHalf);
      invokeAll(firstHalfFJ, secondHalfFJ);
      MSequentialSorter.merge(firstHalfFJ.join(), secondHalfFJ.join(), list);

      return list; // sorted list.
    }

  }


}
