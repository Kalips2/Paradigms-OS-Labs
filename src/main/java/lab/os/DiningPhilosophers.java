package lab.os;

public class DiningPhilosophers {

  private static final int NUM_PHILOSOPHERS = 5;

  private static final Philosopher[] philosophers = new Philosopher[NUM_PHILOSOPHERS];
  private static final Fork[] forks = new Fork[NUM_PHILOSOPHERS];

  public static void main(String[] args) {
    // create forks, amount is NUM_PHILOSOPHERS
    for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
      forks[i] = new Fork();
    }

    for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
      philosophers[i] = new Philosopher(i, forks[i], forks[(i + 1) % NUM_PHILOSOPHERS]);
      new Thread(philosophers[i]).start();
    }
  }

  public static class Philosopher implements Runnable {
    private final int id;
    private final Fork leftFork;
    private final Fork rightFork;

    public Philosopher(int id, Fork leftFork, Fork rightFork) {
      this.id = id;
      this.leftFork = leftFork;
      this.rightFork = rightFork;
    }

    private void doAction(String action) throws InterruptedException {
      String ANSI_RESET = "\u001B[0m";
      String ANSI_RED = "\u001B[31m";
      String ANSI_GREEN = "\u001B[32m";

      String colorCode = action.equals("is eating") ? ANSI_RED : ANSI_GREEN;

      System.out.println(colorCode + "Philosopher " + id + " " + action + ANSI_RESET);
      Thread.sleep((long) (Math.random() * 3500));
    }

    @Override
    public void run() {
      try {
        while (true) {
          doAction("is thinking");
          synchronized (leftFork) {
            // pick up left fork
            synchronized (rightFork) {
              // pick up right fork
              doAction("is eating");
            }
            // right fork is free
          }
          // left fork is free
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

  public static class Fork {
  }
}
