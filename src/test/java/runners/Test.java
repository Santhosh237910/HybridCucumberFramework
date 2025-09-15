package runners;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
//        printDuplicateCountInAString();
//        printDuplicates();
        printDuplicateCharactersFromAString();
    }

    public static void printDuplicateCountInAString() {
        String str = "Sravanthi";
        Map<Character, Integer> map = new HashMap<>();

        for (char ch : str.toCharArray()) {
            map.put(ch, map.getOrDefault(ch, 0) + 1);
//            if (map.containsKey(ch)) {
//                map.put(ch, map.get(ch) + 1);
//            } else {
//                map.put(ch, 1);
//            }
        }
        System.out.println(map);
    }

    public static void printDuplicateCharactersFromAString() {
        String str = "Sravanthi";
        Map<Character, Integer> map = new HashMap<>();

        for (char ch : str.toCharArray()) {
            map.put(ch, map.getOrDefault(ch, 0) + 1);
        }
        System.out.println(map);

        for(Map.Entry<Character,Integer> entry:map.entrySet()){
            if(entry.getValue()>1){
                System.out.println("Duplicate characters:");
                System.out.println(entry.getKey());
            }
        }
    }

    public static void printDuplicates() {
        String str = "Sravanthi";
        char[] ch = str.toCharArray();
        for (int i = 0; i <= ch.length - 1; i++) {
            int count = 1;
            for (int j = i + 1; j <= ch.length - 1; j++) {
                if (ch[i] == ch[j]) {
                    count++;
                }
            }
            System.out.println(ch[i] + " " + count);
        }
    }
}
