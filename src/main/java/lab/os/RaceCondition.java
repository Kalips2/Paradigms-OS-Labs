package lab.os;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RaceCondition {
  private static int sharedVariable = 0;

  private static final Lock lock = new ReentrantLock();

  public static int withCriticalSection() {
    Runnable task = () -> {
      for (int i = 0; i < 1000000000; i++) {
        lock.lock();
        try {
          sharedVariable++;
        } finally {
          lock.unlock();
        }
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

    return sharedVariable;
  }

  public static int withoutCriticalSection() {
    Runnable task = () -> {
      for (int i = 0; i < 1000000000; i++) {
        sharedVariable++;
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

    return sharedVariable;
  }

  public static void main(String[] args) {
    sharedVariable = 0;
    long startTime = System.currentTimeMillis();
    int result = withCriticalSection();
    long endTime = System.currentTimeMillis();
    System.out.println("З критичним сегментом v = " + result + ", заняло часу " + (endTime - startTime) + " ms");

    sharedVariable = 0;
    startTime = System.currentTimeMillis();
    result =  withoutCriticalSection();
    endTime = System.currentTimeMillis();
    System.out.println("Без критичного сегменту v = " + result + ", заняло часу " + (endTime - startTime) + " ms");

  }
}
