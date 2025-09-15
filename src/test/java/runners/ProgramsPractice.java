package runners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProgramsPractice {

    static Logger logger = LogManager.getLogger(ProgramsPractice.class);

    public static void main(String[] args) {
//        int[] arr = {1, 2, 5, 3, 7, 10, 9};
//        int secondLargestNo = findSecondLargestNo(arr);
//        System.out.println(secondLargestNo);
//        int secondLargestNoWithoutSortMethod = findSecondLargestNoWithoutSortingMethod(arr);
//        System.out.println(secondLargestNoWithoutSortMethod);
//        printFibonaciSeries(5);
//        reverseAString("Santhosh");
//        printCountOfDuplicateOccurrencesInAWord("Santhosh");
//        printCountOfDuplicateOccurrencesInAWordUsingEntrySetMethod("Santhosh");
//        printCountOfDuplicateOccurrencesInAWorIgnoringCasing("Santhosh");
//        printCountOfDuplicateOccurrencesInAWordUsingEntrySetMethod("Santhosh");
//        printOnlyDuplicateCharactersInAString("Santhosh");
//        printPalindromePyramidPattern(5);
//        swapTwoNumbersWithoutThirdVariable(5,4);
//        swapTwoStringWithoutUsingThirdVariable("Santhosh", "Kumar");
//        printInExpectedFormatFromAGivenString();
//        capitalizeFirstLetterOfEachWordInASentence("kranthi kumar");
//        capitalizeFirstLetterOfAWordInASentenceEfficientMethod("kranthi kumar");
    }

    public static int findSecondLargestNoFromAnArrayUsingSortMethod(int[] arr) {
        logger.info("Print the second largest number from an array");
        Arrays.sort(arr); // sort the array in ascending order (smallest → largest)
        System.out.println(Arrays.toString(arr)); // print the sorted array
        return arr[arr.length - 2]; // return the second largest element
    }

    //        Algorithm
        /*
        1. compare two first two values which is greater
        2. store largest no in max1 and store largest no in max2
        3. now iterate and compare max1 and max2 with next no starting from 3rd index i,e i=2
        4. if arr[i] is greater than max1 store max1=arr[i] so it will be the largest no
        5. if arr[i] is greater than max2 store max2=arr[i] so it will be the second largest no
        6. iterate until the last index and final output will be largest i.e max1 and second largest i.e max2
        7. now return second largest
         */
    public static int findSecondLargestNoWithoutSortMethod(int[] arr) {
        int max1; // variable to hold the largest number
        int max2; // variable to hold the second largest number

        if (arr[0] > arr[1]) {
            max1 = arr[0]; // assign first element as largest
            max2 = arr[1]; // assign second element as second largest
        } else {
            max2 = arr[0]; // assign first element as second largest
            max1 = arr[1]; // assign second element as largest
        }

        for (int i = 2; i < arr.length; i++) { // start loop from 3rd element
            if (arr[i] > max1) { // if current element is greater than largest
                max2 = max1; // update second largest
                max1 = arr[i]; // update largest
            } else if (arr[i] > max2) { // if current element is greater than second largest
                max2 = arr[i]; // update second largest
            }
        }
        return max2; // return the second largest number
    }


    public static void printFibonaciSeries(int n) {
        logger.info("Print fibonacci series"); // log message for debugging/tracking

        int firstNo = 0; // first number in Fibonacci series
        int secondNo = 1; // second number in Fibonacci series
        int nextNo; // variable to hold the next number

        System.out.print(firstNo + " " + secondNo + " "); // print first two numbers

        for (int i = 2; i <= n; i++) { // loop from 3rd term till n
            nextNo = firstNo + secondNo; // calculate next Fibonacci number
            System.out.print(nextNo + " "); // print next number
            firstNo = secondNo; // shift second number to first
            secondNo = nextNo; // shift next number to second
        }
    }

    public static void reverseAString(String str) {
        logger.info("Reversing a String without using String and StringBuilder"); // log the action

        char[] charArray = str.toCharArray(); // convert string to character array
        char temp; // temporary variable for swapping
        int left = 0; // left pointer (start of array)
        int right = charArray.length - 1; // right pointer (end of array)

        while (left < right) { // loop until pointers meet in the middle
            temp = charArray[left]; // store left character in temp
            charArray[left] = charArray[right]; // copy right character to left position
            charArray[right] = temp; // copy temp (original left) to right position
            left++; // move left pointer forward
            right--; // move right pointer backward
        }

        System.out.println(charArray); // print the reversed string
    }


    public static void printCountOfDuplicateOccurrencesInAWordUsingGetMethod(String str) {
        logger.info("Print Count of Duplicate Occurrences in a Word with get() method"); // log the action

        char[] charArray = str.toCharArray(); // convert string into character array
        Map<Character, Integer> map = new HashMap<>(); // map to store character and its count

        for (char ch : charArray) { // iterate each character
            if (map.containsKey(ch)) { // if character already present in map
                map.put(ch, map.get(ch) + 1); // increment count
            } else {
                map.put(ch, 1); // if character not present, put count = 1
            }
        }
        System.out.println(map); // print the map with character counts
    }


    public static void printCountOfDuplicateOccurrencesInAWordUsingGetOrDefaultMethod(String str) {
        logger.info("Print Count of Duplicate Occurrences in a Word with getOrDefault() method"); // log the action

        char[] charArray = str.toCharArray(); // convert string into character array
        Map<Character, Integer> map = new HashMap<>(); // map to store character and its count

        /* After Java 8 we can use getOrDefault() method to simplify code */
        /* getOrDefault(ch,0) → if char not present, returns 0; then adds 1 */
        for (char ch : charArray) {
            map.put(ch, map.getOrDefault(ch, 0) + 1); // increment count directly
        }

        System.out.println(map); // print the map with character counts
    }


    public static void printCountOfDuplicateOccurrencesInAWorIgnoringCasing(String str) {
        logger.info("Print Count of Duplicate Occurrences in a Word ignoring casing"); // log the action

        char[] charArray = str.toCharArray(); // convert string into character array
        Map<Character, Integer> map = new HashMap<>(); // map to store character and its count

        /* getOrDefault(ch,0) → if char not present in the map, return 0, then add 1 */
        for (char ch : charArray) {
            ch = Character.toLowerCase(ch); // convert to lowercase so 'A' and 'a' are treated same
            map.put(ch, map.getOrDefault(ch, 0) + 1); // increment count
        }

        System.out.println(map); // print the map with counts (case-insensitive)
    }


    public static void printCountOfDuplicateOccurrencesInAWordUsingEntrySetMethod(String str) {
        logger.info("Print Count of Duplicate Occurrences in a Word ignoring casing and using entrySet()"); // log the action

        char[] charArray = str.toCharArray(); // convert string into character array
        Map<Character, Integer> map = new HashMap<>(); // map to store character and its count

        /* getOrDefault(ch,0) → if char not present in the map, return 0, then add 1 */
        for (char ch : charArray) {
            ch = Character.toLowerCase(ch); // convert to lowercase so counting is case-insensitive
            map.put(ch, map.getOrDefault(ch, 0) + 1); // increment count
        }

        /* printing occurrences using entrySet() */
        /* Entry is a nested interface of Map that represents a key-value pair */
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "-" + entry.getValue()); // print character and its count
        }
    }


    public static void printOnlyDuplicateCharactersInAString(String str) {
        logger.info("Print only Duplicate characters in a Word"); // log the action

        char[] charArray = str.toCharArray(); // convert string into character array
        Map<Character, Integer> map = new HashMap<>(); // map to store character and its count

        /* getOrDefault(ch,0) → if char not present, return 0; then add 1 */
        for (char ch : charArray) {
            ch = Character.toLowerCase(ch); // convert to lowercase so 'A' and 'a' are treated same
            map.put(ch, map.getOrDefault(ch, 0) + 1); // increment count
        }

        /* printing only duplicates using entrySet */
        for (Map.Entry<Character, Integer> entry : map.entrySet()) { // iterate key-value pairs
            if (entry.getValue() > 1) { // check if frequency > 1
                System.out.println(entry.getKey()); // print only the duplicate character
            }
        }
    }


    public static void printPalindromePyramidPattern(int n) {
        logger.info("Print Palindrome Pyramid Pattern by given length"); // log the action
    /*
        Example when n = 5:
            1
           212
          32123
         4321234
        543212345
    */
        for (int i = 1; i <= n; i++) { // loop through each row
            for (int s = 1; s <= n - i; s++) { // print leading spaces
                System.out.print(" ");
            }
            for (int j = i; j >= 1; j--) { // print decreasing numbers (left side of pyramid)
                System.out.print(j);
            }
            for (int k = 2; k <= i; k++) { // print increasing numbers (right side of pyramid)
                System.out.print(k);
            }
            System.out.println(""); // move to next line after finishing each row
        }
    }


    public static void swapTwoNumbersWithoutThirdVariable(int a, int b) {
        logger.info("Swap two numbers without using third variable"); // log action
        System.out.println(a + " " + b); // print before swap
        a = a + b; // step 1: sum of both numbers stored in a
        b = a - b; // step 2: subtract original b → gives original a
        a = a - b; // step 3: subtract new b (which is old a) → gives original b
        System.out.println(a + " " + b); // print after swap
    }

    public static void swapTwoStringWithoutUsingThirdVariable(String a, String b) {
        logger.info("Swap two strings without using third variable"); // log action
        System.out.println(a + " " + b); // print before swap
        a = a + b; // concatenate both strings
        b = a.substring(0, a.length() - b.length()); // extract original a
        a = a.substring(b.length()); // extract original b
        System.out.println(a + " " + b); // print after swap
    }

    public static void printInExpectedFormatFromAGivenString() {
        logger.info("Input Java@123 → Output @123AVAj"); // log action
        String s = "Java@123"; // input string
        System.out.println(s); // print original input

        char[] charArray = s.toCharArray(); // convert to char array
        StringBuilder letters = new StringBuilder(); // store letters (for reversing later)
        StringBuilder other = new StringBuilder(); // store non-letter characters

        for (char ch : charArray) { // iterate each character
            if (Character.isLetter(ch)) { // if character is alphabet
                if (Character.isUpperCase(ch)) {
                    letters.append(Character.toLowerCase(ch)); // convert uppercase → lowercase
                } else {
                    letters.append(Character.toUpperCase(ch)); // convert lowercase → uppercase
                }
            } else {
                other.append(ch); // keep digits/special chars as is
            }
        }
        System.out.println(other.append(letters.reverse())); // append reversed letters after others
    }

    public static void capitalizeFirstLetterOfEachWordInASentence(String input) {
        logger.info("Capitalize first letter of each word in a Sentence"); // log action

        String[] strArray = input.trim().split("\\s+ "); // split sentence into words based on spaces
        StringBuilder sb = new StringBuilder(); // StringBuilder to build final result

        for (String str : strArray) { // iterate over each word
            sb.append(str.substring(0, 1).toUpperCase()) // take first letter and convert to uppercase
                    .append(str.substring(1).toLowerCase())    // convert rest of the word to lowercase
                    .append(" "); // add space after each word
        }

        System.out.println(sb.toString().trim()); // print final string (trim removes last extra space)
    }

    public static void capitalizeFirstLetterOfAWordInASentenceEfficientMethod(String input) {
        logger.info("Capitalize first letter of each word in a Sentence using efficient method"); // log action

        StringBuilder sb = new StringBuilder(input.length()); // pre-allocate buffer for efficiency
        boolean capitalized = true; // flag to indicate next character should be uppercase

        for (char ch : input.toCharArray()) { // iterate over each character
            if (Character.isWhitespace(ch)) { // if space, keep as is
                sb.append(ch); // append space
                capitalized = true; // next non-space character should be capitalized
            } else if (capitalized) { // if flag is true, capitalize this character
                sb.append(Character.toUpperCase(ch));
                capitalized = false; // reset flag
            } else { // otherwise, lowercase the character
                sb.append(Character.toLowerCase(ch));
            }
        }

        System.out.println(sb); // print final capitalized sentence
    }



}
