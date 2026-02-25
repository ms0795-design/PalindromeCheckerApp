public class PalindromeCheckerCharArray {
    public static void main(String[] args) {
        String str = "level";
        
        // Convert string to character array
        char[] charArray = str.toCharArray();
        boolean isPalindrome = true;
        
        // Use two-pointer approach
        int start = 0;
        int end = charArray.length - 1;
        
        while (start < end) {
            if (charArray[start] != charArray[end]) {
                isPalindrome = false;
                break;
            }
            start++;
            end--;
        }
        
        // Display result
        if (isPalindrome) {
            System.out.println(str + " is a palindrome.");
        } else {
            System.out.println(str + " is not a palindrome.");
        }
    }
}