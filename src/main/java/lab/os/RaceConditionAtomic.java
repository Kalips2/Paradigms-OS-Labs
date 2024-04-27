package lab.os;

import java.util.concurrent.atomic.AtomicInteger;

public class RaceConditionAtomic {
  private static AtomicInteger sharedVariable = new AtomicInteger(0);

  public static void withCriticalSection() {
    Runnable task = () -> {
      for (int i = 0; i < 1000000000; i++) {
        sharedVariable.incrementAndGet();
      }
    };

    Thread thread1 = new Thread(task);
    Thread thread2 = new Thread(task);

    thread1.start();
    thread2.start();

    try {
      thread1.join();
      thread2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    long startTime = System.currentTimeMillis();
    withCriticalSection();
    long endTime = System.currentTimeMillis();
    System.out.println(
        "З критичним сегментом v = " + sharedVariable.get() + ", заняло часу " + (endTime - startTime) + " ms");
  }
}
