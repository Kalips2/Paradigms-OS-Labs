package lab.os;

public class RaceConditionSynchronized {
  private static final int TARGET_VALUE = 1000;
  private static int sharedVariable = 0;

  public static void main(String[] args) {
    Thread adderThread1 = new Thread(() -> {
      while (sharedVariable < TARGET_VALUE) {
        synchronized (RaceConditionSynchronized.class) {
          if (sharedVariable < TARGET_VALUE) {
            sharedVariable++;
            System.out.println("Incrementing from " + Thread.currentThread().getName());
          }
          RaceConditionSynchronized.class.notify();
        }
      }
    });

    Thread adderThread2 = new Thread(() -> {
      while (sharedVariable < TARGET_VALUE) {
        synchronized (RaceConditionSynchronized.class) {
          if (sharedVariable < TARGET_VALUE) {
            sharedVariable++;
            System.out.println("Incrementing from " + Thread.currentThread().getName());
          }
          RaceConditionSynchronized.class.notify();
        }
      }
    });

    adderThread1.start();
    adderThread2.start();

    try {
      adderThread1.join();
      adderThread2.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("Результат додавання: " + sharedVariable);
  }
}
