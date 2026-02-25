public class PalindromeCheckerReverse {
    public static void main(String[] args) {
        String str = "madam";
        String reversed = "";

        // Reverse the string using a loop
        for (int i = str.length() - 1; i >= 0; i--) {
            reversed += str.charAt(i);
        }

        // Compare original and reversed strings
        if (str.equals(reversed)) {
            System.out.println(str + " is a palindrome.");
        } else {
            System.out.println(str + " is not a palindrome.");
        }
    }
}