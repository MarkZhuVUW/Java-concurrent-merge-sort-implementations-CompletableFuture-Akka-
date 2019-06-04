
package ass1;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * performance:
 * ----------------------------------------------------------------------------------------------------
 * ----------------------------------------------------------------------------------------------------
 * On the data type Float
 * Sequential merge sort sort takes 2.808 seconds
 * Parallel merge sort (futures) sort takes 3.578 seconds
 * Parallel merge sort (forkJoin) sort takes 1.5 seconds
 *
 * On the data type Point
 * Sequential merge sort sort takes 6.085 seconds
 * Parallel merge sort (futures) sort takes 4.601 seconds
 * Parallel merge sort (forkJoin) sort takes 2.287 seconds
 *
 * On the data type BigInteger
 * Sequential merge sort sort takes 4.112 seconds
 * Parallel merge sort (futures) sort takes 3.43 seconds
 * Parallel merge sort (forkJoin) sort takes 1.535 seconds
 *
 * On the data type String
 * Sequential merge sort sort takes 6.334 seconds
 * Parallel merge sort (futures) sort takes 4.776 seconds
 * Parallel merge sort (forkJoin) sort takes 2.402 seconds
 *
 * ----------------------------------------------------------------------------------------------------
 * ----------------------------------------------------------------------------------------------------
 *  Generally the parallelSorter2 had the best performance, parallelsorter1 runner-up and sequential merge sorter third place in all tests in TestPerformance class.
 *  This is probably because:
 *  1.sorter2 utilizes work stealing so that it is faster than others.
 *  2.The reason for parallelsorter1 being in second place is that the newcachedthreadpool uses only one common task queue, which is bottlenecked. In other words, all threads are trying to get work from a common task queue, and the probability of multiple thread trying to get work simultaneously is higher. So in this case the newCachedPool creates much more threads than the forkjoinpool, which slows down performance.
 *  And since the dataset is not big enough or my computer is not suited for multi-threading, the parallelsorter1, in the float tests is slower than the sequential method.
 *
 *  the pros and cons of the alternative implementations:
 *  1. I have tried to compare in ParallelSorter1 the costs to fork both both halves with only forking one half. The result shows that forking only one half is more efficient. This is probably because if both two halves are forked,
 *  the current thread would be kind of idle, thus speed is decreased.
 *  2. for the parallelsorters I distribute the < 20 lists to be sorted by the sequential merge sort. This, in my case, increased performance.
 *
 */
public class TestPerformance {

  /**
   * This method takes in a functional interface Runnable, and two integer values warmUp and runs.
   * The method first does garbage collection so that hopefully the garbage collection thread will not interact with the process later on.
   * Then it does what is specified in the Runnable r for warmUp times, records the current time in millisecond, does what's specified by r again for runs times and then record time spent. The purpose of this is to enable the JIT.
   * what JIT does is that if a program is run for many many times it sort of optimizes it so that for our merge sort example, the sorting runs faster.
   *    * @param r
   * @param warmUp number of times to initialize JIT
   * @param runs number of times to optimize mergesort
   * @return return the time taken
   */
  long timeOf(Runnable r,int warmUp,int runs) {
    System.gc();
    for(int i=0;i<warmUp;i++) {r.run();}
    long time0=System.currentTimeMillis();
    for(int i=0;i<runs;i++) {r.run();}
    long time1=System.currentTimeMillis();
    return time1-time0;
  }

  /**
   *  This method takes in a Sorter, a string and a 2d array of T. s, name, dataset respectively.
   *  Then it performs the sort method using the given sorter s for every kind dataset and record time spent.
   * @param s a kind of sorter object
   * @param name name of the sorter
   * @param dataset the dataset for testing
   * @param <T> a generic type
   */
  <T extends Comparable<? super T>>void msg(Sorter s,String name,T[][] dataset) {
    long time=timeOf(()->{
      for(T[]l:dataset){s.sort(Arrays.asList(l));}
      },200,200);//realistically 20.000 to make the JIT do his job..
      System.out.println(name+" sort takes "+time/1000d+" seconds");
    }


  /**
   * takes in a 2d array of T and runs msg method on sequential merge sorter, parallel sorter with futures and parallel sorter with forkjoin framework.
   * @param dataset dataset for testing
   * @param <T> a generic type
   */
  <T extends Comparable<? super T>>void msgAll(T[][] dataset) {
    //msg(new ISequentialSorter(),"Sequential insertion",TestBigInteger.dataset);//so slow
    //uncomment the former line to include performance of ISequentialSorter
    msg(new MSequentialSorter(),"Sequential merge sort",dataset);
    msg(new MParallelSorter1(),"Parallel merge sort (futures)",dataset);
    msg(new MParallelSorter2(),"Parallel merge sort (forkJoin)",dataset);
    }

  /**
   * tests the 3 sorters with the BigInteger dataset.
   */
  @Test
  void testBigInteger() {
    System.out.println("On the data type BigInteger");
    msgAll(TestBigInteger.dataset);
    }

  /**
   * tests the 3 sorters with the Float dataset.
   */
  @Test
  void testFloat() {
    System.out.println("On the data type Float");
    msgAll(TestFloat.dataset);
    }

  /**
   * tests the 3 sorters with the Point dataset.
   */
  @Test
  void testPoint() {
    System.out.println("On the data type Point");
    msgAll(TestPoint.dataset);
    }

  /**
   * tests the 3 sorters with the String dataset.
   */
  @Test
  void testString() {
    System.out.println("On the data type String");
    msgAll(TestPoint.dataset);
  }
  }