package programmingtests;

import org.junit.jupiter.api.Test;
import org.moses.programming.DigitalRoot;
import org.moses.programming.RemoveDuplicates;
import org.moses.programming.TimeInWords;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {

    @Test
    public void testTimeInWordsIntegration() {
        // Test the complete flow from input to output
        String result = TimeInWords.convertTimeToWords(5, 47);
        assertEquals("thirteen minutes to six", result.toLowerCase());
    }

    @Test
    void testRemoveDuplicatesIntegration() {
        int[][] complexInput = {
            {1, 2, 3, 1, 2, 3},
            {5, 5, 5},
            {1, 2, 3, 4, 5}
        };
        
        int[][] result = RemoveDuplicates.removeDuplicates(complexInput);
        
        assertEquals(3, result.length);
        assertArrayEquals(new int[]{1, 2, 3, 0, 0, 0}, result[0]);
        assertArrayEquals(new int[]{5, 0, 0}, result[1]);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, result[2]);
    }

    @Test
    void testDigitalRootIntegration() {
        // Test the complete digital root calculation
        String largeNumber = "987654321";
        int sum = DigitalRoot.sumOfDigits(largeNumber);
        int root = DigitalRoot.digitalRoot(largeNumber);
        
        assertEquals(45, sum);
        assertEquals(9, root);
    }
}