package org.moses.programming;

public class DigitalRoot {
    
    /**
     * Part A: Recursive function to find sum of digits
     * Time Complexity: O(n) where n is length of the string
     * Space Complexity: O(n) due to recursion stack
     */
    public static int sumOfDigits(String digits) {
        if (digits == null || digits.length() == 0) {
            return 0;
        }
        
        // Base case: single digit
        if (digits.length() == 1) {
            return Character.getNumericValue(digits.charAt(0));
        }
        
        // Process first digit and recurse on the rest
        int firstDigit = Character.getNumericValue(digits.charAt(0));
        String remaining = digits.substring(1);
        
        return firstDigit + sumOfDigits(remaining);
    }
    
    /**
     * Part B: Find digital root using iteration
     * Time Complexity: O(log(n)) in most cases
     * Space Complexity: O(1)
     */
    public static int digitalRoot(String digits) {
        if (digits == null || digits.length() == 0) {
            return 0;
        }
        
        long currentSum = sumOfDigits(digits);
        
        // Keep summing until we get a single digit
        while (currentSum >= 10) {
            currentSum = sumOfDigits(String.valueOf(currentSum));
        }
        
        return (int) currentSum;
    }
    
    /**
     * Alternative iterative implementation for digital root
     * Using mathematical property: digital root = 1 + (num - 1) % 9
     * More efficient for very large numbers
     */
    public static int digitalRootEfficient(String digits) {
        if (digits == null || digits.length() == 0) {
            return 0;
        }
        
        long sum = 0;
        for (char c : digits.toCharArray()) {
            sum += Character.getNumericValue(c);
        }
        
        // Mathematical formula for digital root
        if (sum == 0) return 0;
        return (int) (1 + (sum - 1) % 9);
    }
    
    public static void main(String[] args) {
        String sampleInput = "1234445123444512344451234445123444512344451234445";
        
        System.out.println("Input: " + sampleInput);
        System.out.println("Sum of digits: " + sumOfDigits(sampleInput));
        System.out.println("Digital root: " + digitalRoot(sampleInput));
        System.out.println("Digital root (efficient): " + digitalRootEfficient(sampleInput));
    }
}