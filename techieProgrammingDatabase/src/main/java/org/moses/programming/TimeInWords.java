package org.moses.programming;

public class TimeInWords {
    private static final String[] numbers = {
            "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
            "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen",
            "eighteen", "nineteen", "twenty", "twenty-one", "twenty-two", "twenty-three",
            "twenty-four", "twenty-five", "twenty-six", "twenty-seven", "twenty-eight", "twenty-nine"
    };

    public static String convertTimeToWords(int h, int m) {
        // Validate inputs
        if (h < 1 || h > 12) {
            throw new IllegalArgumentException("Hours must be between 1 and 12");
        }
        if (m < 0 || m > 59) {
            throw new IllegalArgumentException("Minutes must be between 0 and 59");
        }

        if (m == 0) {
            return numbers[h] + " o'clock";
        } else if (m == 15 || m == 30 || m == 45) {
            String[] phrases = {"quarter past ", "half past ", "quarter to "};
            return phrases[m / 15 - 1] + numbers[m == 45 ? h % 12 + 1 : h];
        } else if (m == 1 || m == 59) {
            String phrase = m == 1 ? "one minute past " : "one minute to ";
            return phrase + numbers[m == 59 ? h % 12 + 1 : h];
        } else {
            boolean isPast = m < 30;
            int minutes = isPast ? m : 60 - m;
            String phrase = isPast ? " minutes past " : " minutes to ";
            String minuteText = minutes <= 20 ? numbers[minutes] : "twenty-" + numbers[minutes - 20];
            return minuteText + phrase + numbers[isPast ? h : h % 12 + 1];
        }
    }

    public static void main(String[] args) {
        try {
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            System.out.print("Enter hours: ");
            int h = scanner.nextInt();
            System.out.print("Enter minutes: ");
            int m = scanner.nextInt();

            String result = convertTimeToWords(h, m);
            // Capitalize first letter
            result = result.substring(0, 1).toUpperCase() + result.substring(1);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}