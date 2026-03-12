import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

interface PalindromeStrategy {
    boolean check(String input);
}

class StackStrategy implements PalindromeStrategy {
    @Override
    public boolean check(String input) {
        if (input == null) return false;
        String str = input.replaceAll("\\s+", "").toLowerCase();

        Stack<Character> stack = new Stack<>();
        for (char ch : str.toCharArray()) {
            stack.push(ch);
        }
        for (char ch : str.toCharArray()) {
            if (ch != stack.pop()) return false;
        }

        return true;
    }
}

class DequeStrategy implements PalindromeStrategy {
    @Override
    public boolean check(String input) {
        if (input == null) return false;
        String str = input.replaceAll("\\s+", "").toLowerCase();
        Deque<Character> deque = new LinkedList<>();
        for (char ch : str.toCharArray()) {
            deque.addLast(ch);
        }

        while (deque.size() > 1) {
            if (deque.removeFirst() != deque.removeLast()) return false;
        }

        return true;
    }
}

class PalindromeChecker {
    private PalindromeStrategy strategy;
    public PalindromeChecker(PalindromeStrategy strategy) {
        this.strategy = strategy;
    }
    public void setStrategy(PalindromeStrategy strategy) {
        this.strategy = strategy;
    }
    public boolean checkPalindrome(String input) {
        return strategy.check(input);
    }
}
public class PalindroneCheckerApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a string: ");
        String input = sc.nextLine();

        PalindromeChecker stackChecker = new PalindromeChecker(new StackStrategy());
        PalindromeChecker dequeChecker = new PalindromeChecker(new DequeStrategy());

        long startStack = System.nanoTime();
        boolean stackResult = stackChecker.checkPalindrome(input);
        long endStack = System.nanoTime();
        long stackTime = endStack - startStack;

        long startDeque = System.nanoTime();
        boolean dequeResult = dequeChecker.checkPalindrome(input);
        long endDeque = System.nanoTime();
        long dequeTime = endDeque - startDeque;

        System.out.println("\n=== Palindrome Check Results ===");
        System.out.println("Using Stack Strategy: " + (stackResult ? "Palindrome" : "Not Palindrome") +
                " | Time: " + stackTime + " ns");
        System.out.println("Using Deque Strategy: " + (dequeResult ? "Palindrome" : "Not Palindrome") +
                " | Time: " + dequeTime + " ns");

        sc.close();
    }
}