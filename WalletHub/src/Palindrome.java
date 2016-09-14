public class Palindrome {
    public static void main(String[] args) {
        String test = "А роза упала на лапу азора";
        System.out.println(isPalindrome(test));
    }

    private static boolean isPalindrome(String testString) {
        String test = testString.toLowerCase().replaceAll("\\s+", "");
        for (int i = 0; i < test.length(); i++) {
            if ((test.charAt(i) != test.charAt(test.length() - i - 1))) return false;
        }
        return true;
    }
}
