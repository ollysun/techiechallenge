package programmingtests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.moses.programming.TimeInWords;

class TimeInWordsTest {

  @Test
  void testOClock() {
    assertEquals("five o'clock", TimeInWords.convertTimeToWords(5, 0));
  }

  @Test
  void testQuarterPast() {
    assertEquals("quarter past five", TimeInWords.convertTimeToWords(5, 15));
  }

  @Test
  void testHalfPast() {
    assertEquals("half past five", TimeInWords.convertTimeToWords(5, 30));
  }

  @Test
  void testQuarterTo() {
    assertEquals("quarter to six", TimeInWords.convertTimeToWords(5, 45));
  }

  @Test
  void testOneMinutePast() {
    assertEquals("one minute past five", TimeInWords.convertTimeToWords(5, 1));
  }

  @Test
  void testTenMinutesPast() {
    assertEquals("ten minutes past five", TimeInWords.convertTimeToWords(5, 10));
  }

  @Test
  void testTwentyMinutesTo() {
    assertEquals("twenty minutes to six", TimeInWords.convertTimeToWords(5, 40));
  }

  @Test
  void testThirteenMinutesTo() {
    assertEquals("thirteen minutes to six", TimeInWords.convertTimeToWords(5, 47));
  }

  @Test
  void testTwentyEightMinutesPast() {
    assertEquals("twenty-eight minutes past five", TimeInWords.convertTimeToWords(5, 28));
  }

  @Test
  void testEdgeCase59Minutes() {
    assertEquals("one minute to six", TimeInWords.convertTimeToWords(5, 59));
  }

  @Test
  void testEdgeCase29Minutes() {
    assertEquals("twenty-nine minutes past five", TimeInWords.convertTimeToWords(5, 29));
  }

  @Test
  void testEdgeCase31Minutes() {
    assertEquals("twenty-nine minutes to six", TimeInWords.convertTimeToWords(5, 31));
  }

  @Test
  void testInvalidHours() {
    assertThrows(IllegalArgumentException.class, () -> TimeInWords.convertTimeToWords(13, 30));
    assertThrows(IllegalArgumentException.class, () -> TimeInWords.convertTimeToWords(0, 30));
  }

  @Test
  void testInvalidMinutes() {
    assertThrows(IllegalArgumentException.class, () -> TimeInWords.convertTimeToWords(5, 60));
    assertThrows(IllegalArgumentException.class, () -> TimeInWords.convertTimeToWords(5, -1));
  }

  @Test
  void testBoundaryCases() {
    assertEquals("twelve o'clock", TimeInWords.convertTimeToWords(12, 0));
    assertEquals("one minute to one", TimeInWords.convertTimeToWords(12, 59));
  }
}
