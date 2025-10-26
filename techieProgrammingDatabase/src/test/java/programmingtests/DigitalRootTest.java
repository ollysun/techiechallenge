package programmingtests; // DigitalRootTest.java

import org.junit.jupiter.api.Test;
import org.moses.programming.DigitalRoot;

import static org.junit.jupiter.api.Assertions.*;

class DigitalRootTest {

    @Test
    void testSumOfDigits() {
        assertEquals(1, DigitalRoot.sumOfDigits("1"));
        assertEquals(15, DigitalRoot.sumOfDigits("12345"));
        assertEquals(10, DigitalRoot.sumOfDigits("55"));
        assertEquals(0, DigitalRoot.sumOfDigits(""));
        assertEquals(0, DigitalRoot.sumOfDigits(null));
    }

    @Test
    void testSumOfDigitsLargeNumber() {
        String largeNumber = "1234445123444512344451234445123444512344451234445";
        assertEquals(161, DigitalRoot.sumOfDigits(largeNumber));
    }

    @Test
    void testDigitalRoot() {
        assertEquals(5, DigitalRoot.digitalRoot("1234445"));
        assertEquals(9, DigitalRoot.digitalRoot("9"));
        assertEquals(9, DigitalRoot.digitalRoot("18"));
        assertEquals(9, DigitalRoot.digitalRoot("99"));
        assertEquals(6, DigitalRoot.digitalRoot("123"));
        assertEquals(0, DigitalRoot.digitalRoot(""));
        assertEquals(0, DigitalRoot.digitalRoot(null));
    }

    @Test
    void testDigitalRootEfficient() {
        assertEquals(5, DigitalRoot.digitalRootEfficient("1234445"));
        assertEquals(9, DigitalRoot.digitalRootEfficient("9"));
        assertEquals(9, DigitalRoot.digitalRootEfficient("18"));
        assertEquals(9, DigitalRoot.digitalRootEfficient("99"));
        assertEquals(6, DigitalRoot.digitalRootEfficient("123"));
        assertEquals(0, DigitalRoot.digitalRootEfficient(""));
        assertEquals(0, DigitalRoot.digitalRootEfficient(null));
    }

    @Test
    void testConsistencyBetweenMethods() {
        String[] testCases = {
            "1234445", "999", "123456789", "1", "10", "100", "1000"
        };
        
        for (String testCase : testCases) {
            assertEquals(
                DigitalRoot.digitalRoot(testCase),
                DigitalRoot.digitalRootEfficient(testCase),
                "Mismatch for input: " + testCase
            );
        }
    }
}