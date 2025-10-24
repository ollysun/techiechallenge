package org.moses.programming;

public class RemoveDuplicates {

  /**
   * Removes duplicates from each row of a 2D array by replacing duplicates with 0. Uses a custom
   * implementation without built-in contains functions.
   *
   * <p>Approach: 1. For each row, we'll use a custom array to track seen elements 2. First
   * occurrence of an element is kept, subsequent occurrences are set to 0 3. We implement our own
   * search function to check if we've seen an element before
   *
   * <p>Time Complexity: O(n * m^2) where n is number of rows, m is max row length Space Complexity:
   * O(m) for tracking seen elements per row
   */
  public static int[][] removeDuplicates(int[][] array) {
    if (array == null || array.length == 0) {
      return new int[0][];
    }

    int[][] result = new int[array.length][];

    for (int i = 0; i < array.length; i++) {
      if (array[i] == null || array[i].length == 0) {
        result[i] = new int[0];
        continue;
      }

      result[i] = new int[array[i].length];
      int[] seen = new int[array[i].length];
      int seenCount = 0;

      for (int j = 0; j < array[i].length; j++) {
        int current = array[i][j];

        // Check if we've seen this element before in current row
        boolean isDuplicate = false;
        for (int k = 0; k < seenCount; k++) {
          if (seen[k] == current) {
            isDuplicate = true;
            break;
          }
        }

        if (!isDuplicate) {
          // First occurrence - keep value and add to seen
          result[i][j] = current;
          seen[seenCount++] = current;
        } else {
          // Duplicate - set to 0
          result[i][j] = 0;
        }
      }
    }

    return result;
  }

  public static void printArray(int[][] array) {
    System.out.print("[");
    for (int i = 0; i < array.length; i++) {
      System.out.print("{");
      for (int j = 0; j < array[i].length; j++) {
        System.out.print(array[i][j]);
        if (j < array[i].length - 1) {
          System.out.print(", ");
        }
      }
      System.out.print("}");
      if (i < array.length - 1) {
        System.out.print(", ");
      }
    }
    System.out.println("]");
  }

  public static void main(String[] args) {
    int[][] input = {
      {1, 3, 1, 2, 3, 4, 4, 3, 5},
      {1, 1, 1, 1, 1, 1, 1}
    };

    System.out.println("Input:");
    printArray(input);

    int[][] output = removeDuplicates(input);

    System.out.println("Output:");
    printArray(output);
  }
}
