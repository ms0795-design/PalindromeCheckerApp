import java.util.Stack;

public class PalindromeCheckerStack {
    public static boolean isPalindrome(String input) {
        Stack<Character> stack = new Stack<>();
        for (char c : input.toCharArray()) {
            stack.push(c);
        }

        for (char c : input.toCharArray()) {
            if (c != stack.pop()) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String test = "racecar";
        System.out.println(test + " is palindrome? " + isPalindrome(test));
    }
}