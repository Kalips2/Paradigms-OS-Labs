package lab.os;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixMultiplication {
  public static void main(String[] args) {
    int n = 100;
    int m = 100;
    int k = 100;

    int[][] A = generateRandomMatrix(n, m);
    System.out.println("Matrix A:");
    printMatrix(A);
    int[][] B = generateRandomMatrix(m, k);
    System.out.println("Matrix B:");
    printMatrix(B);

    int[][] C = new int[n][k];

    int numThreads = 5;

    ExecutorService executor = Executors.newFixedThreadPool(numThreads);

    long startTime = System.currentTimeMillis();
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < k; j++) {
        final int row = i;
        final int col = j;
        executor.submit(() -> {
          int sum = 0;
          for (int l = 0; l < m; l++) {
            sum += A[row][l] * B[l][col];
          }
          C[row][col] = sum;
          System.out.println("[" + row + "," + col + "]=" + sum  + ", calculated at " + Thread.currentThread().getName());
        });
      }
    }

    executor.shutdown();

    try {
      executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      System.err.println("Помилка при очікуванні завершення потоків: " + e.getMessage());
    }

    long endTime = System.currentTimeMillis();

    System.out.println("\nРезультуюча матриця C:");
    printMatrix(C);
    System.out.println("Час виконання: " + (endTime - startTime) + " мілісекунд");
  }

  public static int[][] generateRandomMatrix(int rows, int cols) {
    int[][] matrix = new int[rows][cols];
    Random random = new Random();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        matrix[i][j] = random.nextInt(1000) - 100;
      }
    }
    return matrix;
  }

  public static void printMatrix(int[][] matrix) {
    for (int[] row : matrix) {
      for (int value : row) {
        System.out.printf("%10d ", value);
      }
      System.out.println();
    }
  }
}
