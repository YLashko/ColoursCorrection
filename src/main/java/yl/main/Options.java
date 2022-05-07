package yl.main;

import java.util.HashMap;
public class Options {
    public static String numbers = "1234567890.";
    public static String operators = "+-/*^%><=!";
    public static String vars = "qwertyuiopasdfghjklzxcvbnm_QWERTYUIOPASDFGHJKLZXCVBNM";
    public static String openingBracket = "(";
    public static String closingBracket = ")";
    public static String ternary = "?:";
    public static String imagesFolder = System.getProperty("user.dir") + "/src/main/java/yl/main/images/";
    public static HashMap<String, String> chars = fillHashMap();

    private static HashMap<String, String> fillHashMap(){
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < numbers.length(); i++){
            map.put(Character.toString(numbers.charAt(i)), "n");
        }
        for (int i = 0; i < operators.length(); i++){
            map.put(Character.toString(operators.charAt(i)), "o");
        }
        for (int i = 0; i < vars.length(); i++){
            map.put(Character.toString(vars.charAt(i)), "v");
        }
        for (int i = 0; i < ternary.length(); i++){
            map.put(Character.toString(ternary.charAt(i)), "t");
        }
        map.put(openingBracket, "ob");
        map.put(closingBracket, "cb");

        return map;
    }
}
