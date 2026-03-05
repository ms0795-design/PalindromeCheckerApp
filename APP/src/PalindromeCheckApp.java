import java.util.Queue;
import java.util.Stack;
import java.util.LinkedList;

public class PalindromeCheckApp {

        static class Node {
            char data;
            Node next;

            Node(char data) {
                this.data = data;
                this.next = null;
            }
        }

        public static Node createLinkedList(String str) {
            Node head = null, tail = null;

            for (int i = 0; i < str.length(); i++) {
                Node newNode = new Node(str.charAt(i));

                if (head == null) {
                    head = newNode;
                    tail = newNode;
                } else {
                    tail.next = newNode;
                    tail = newNode;
                }
            }
            return head;
        }

        public static Node reverse(Node head) {
            Node prev = null;
            Node current = head;
            Node next;

            while (current != null) {
                next = current.next;
                current.next = prev;
                prev = current;
                current = next;
            }

            return prev;
        }

        public static boolean isPalindrome(Node head) {

            if (head == null || head.next == null)
                return true;

            Node slow = head;
            Node fast = head;

            // Find middle of linked list
            while (fast.next != null && fast.next.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }

            Node secondHalf = reverse(slow.next);

            Node firstHalf = head;
            Node temp = secondHalf;

            while (temp != null) {
                if (firstHalf.data != temp.data)
                    return false;

                firstHalf = firstHalf.next;
                temp = temp.next;
            }

            return true;
        }

        public static void main(String[] args) {

            Scanner sc = new Scanner(System.in);

            System.out.print("Enter a string: ");
            String input = sc.nextLine();

            Node head = createLinkedList(input);

            if (isPalindrome(head))
                System.out.println("Palindrome");
            else
                System.out.println("Not a Palindrome");

            sc.close();
        }
    }



