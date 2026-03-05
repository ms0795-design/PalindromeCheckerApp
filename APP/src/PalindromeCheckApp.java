import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.LinkedList;

public class PalindromeCheckApp {


        public static boolean isPalindrome(String str, int start, int end) {

            if (start >= end) {
                return true;
            }

            if (str.charAt(start) != str.charAt(end)) {
                return false;
            }

            return isPalindrome(str, start + 1, end - 1);
        }

        public static void main(String[] args) {

            Scanner sc = new Scanner(System.in);

            System.out.print("Enter a string: ");
            String str = sc.nextLine();

            boolean result = isPalindrome(str, 0, str.length() - 1);

            if (result) {
                System.out.println("The given string \"" + str + "\" is a Palindrome.");
            } else {
                System.out.println("The given string \"" + str + "\" is NOT a Palindrome.");
            }

            sc.close();
        }
    }
