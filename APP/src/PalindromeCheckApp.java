import java.util.Scanner;

public class PalindromeCheckApp {

    // Method to check if a string is a palindrome
    public static boolean isPalindrome(String text) {
        // Remove non-alphanumeric characters and convert to lowercase
        String cleaned = text.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        // Reverse the string
        String reversed = new StringBuilder(cleaned).reverse().toString();

        return cleaned.equals(reversed);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Palindrome Checker App ===");

        while (true) {
            System.out.print("\nEnter a word or sentence (or type 'exit' to quit): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            }

            if (isPalindrome(input)) {
                System.out.println("✅ It is a palindrome!");
            } else {
                System.out.println("❌ It is NOT a palindrome.");
            }
        }

        scanner.close();
    }
} //test
