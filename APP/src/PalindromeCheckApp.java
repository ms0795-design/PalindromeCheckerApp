import java.util.Queue;
import java.util.Stack;
import java.util.LinkedList;

public class PalindromeCheckApp {
    public static void main(String[] args) {
        String input = "racecar";

        // Using Queue (FIFO - First In First Out)
        Queue<Character> queue = new LinkedList<>();
        for (char c : input.toCharArray()) {
            queue.add(c);
        }

        // Using Stack (LIFO - Last In First Out)
        Stack<Character> stack = new Stack<>();
        for (char c : input.toCharArray()) {
            stack.push(c);
        }

        System.out.println("Input: " + input);
        System.out.println("\nQueue (FIFO) Output:");
        while (!queue.isEmpty()) {
            System.out.print(queue.poll());
        }

        System.out.println("\nStack (LIFO) Output:");
        while (!stack.isEmpty()) {
            System.out.print(stack.pop());
        }

        // Compare outputs for palindrome validation
        System.out.println("\n\nPalindrome Check:");
        Queue<Character> queueCheck = new LinkedList<>();
        Stack<Character> stackCheck = new Stack<>();

        for (char c : input.toCharArray()) {
            queueCheck.add(c);
            stackCheck.push(c);
        }

        boolean isPalindrome = true;
        while (!queueCheck.isEmpty() && !stackCheck.isEmpty()) {
            if (queueCheck.poll() != stackCheck.pop()) {
                isPalindrome = false;
                break;
            }
        }

        if (isPalindrome) {
            System.out.println(input + " is a palindrome (Queue and Stack outputs match in reverse)");
        } else {
            System.out.println(input + " is not a palindrome");
        }
    }
}