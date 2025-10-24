package programmingtests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.moses.programming.RemoveDuplicates;

class RemoveDuplicatesTest {

  @Test
  void testSampleInput() {
    int[][] input = {
      {1, 3, 1, 2, 3, 4, 4, 3, 5},
      {1, 1, 1, 1, 1, 1, 1}
    };

    int[][] expected = {
      {1, 3, 0, 2, 0, 4, 0, 0, 5},
      {1, 0, 0, 0, 0, 0, 0}
    };

    assertArrayEquals(expected, RemoveDuplicates.removeDuplicates(input));
  }

  @Test
  void testEmptyArray() {
    int[][] input = {};
    int[][] expected = {};
    assertArrayEquals(expected, RemoveDuplicates.removeDuplicates(input));
  }

  @Test
  void testNullArray() {
    int[][] input = null;
    int[][] expected = new int[0][];
    assertArrayEquals(expected, RemoveDuplicates.removeDuplicates(input));
  }

  @Test
  void testEmptyRows() {
    int[][] input = {{}, {1, 2}, {}};
    int[][] expected = {{}, {1, 2}, {}};
    assertArrayEquals(expected, RemoveDuplicates.removeDuplicates(input));
  }

  @Test
  void testNoDuplicates() {
    int[][] input = {{1, 2, 3}, {4, 5, 6}};
    int[][] expected = {{1, 2, 3}, {4, 5, 6}};
    assertArrayEquals(expected, RemoveDuplicates.removeDuplicates(input));
  }

  @Test
  void testAllDuplicates() {
    int[][] input = {{5, 5, 5}, {7, 7}};
    int[][] expected = {{5, 0, 0}, {7, 0}};
    assertArrayEquals(expected, RemoveDuplicates.removeDuplicates(input));
  }

  @Test
  void testMixedDuplicates() {
    int[][] input = {{0, 1, 0, 2, 1, 3}};
    int[][] expected = {{0, 1, 0, 2, 0, 3}};
    assertArrayEquals(expected, RemoveDuplicates.removeDuplicates(input));
  }

  @Test
  void testLargeNumbers() {
    int[][] input = {{1000, 500, 1000, 500, 2000}};
    int[][] expected = {{1000, 500, 0, 0, 2000}};
    assertArrayEquals(expected, RemoveDuplicates.removeDuplicates(input));
  }
}
