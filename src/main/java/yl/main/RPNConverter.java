package yl.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Character;
import java.lang.Integer;
import java.lang.String;
import java.util.Objects;

public class RPNConverter {

    private String openingBracket = Options.openingBracket;
    private HashMap<String, String> chars = Options.chars;
    private HashMap<String, String> priorities = new HashMap<String, String>();

    RPNConverter(){
        fillHashMap();
    }

    public ArrayList<String> toRPN(String expression){
        ArrayList<String> exp = divide(expression);
        ArrayList<String> stack = new ArrayList<String>();
        ArrayList<String> result = new ArrayList<String>();
        for (String el: exp){
            switch (chars.get(el.substring(0, 1))){

                case "n":
                    result.add(el);
                break;

                case "o": 
                    if(stack.size() > 0){
                        if (Integer.parseInt(priorities.get(el)) <= Integer.parseInt(priorities.get(stack.get(stack.size() - 1)))){
                            for (int i = stack.size() - 1; i >= 0; i--){
                                if (stack.get(i).contains(openingBracket)){
                                    break;
                                } else {
                                    result.add(stack.get(i));
                                    stack.remove(i);
                                }
                            }
                        }
                    }
                    stack.add(el);
                break;

                case "v":
                    result.add(el);
                break;

                case "ob":
                    stack.add(el);
                break;

                case "cb":
                    for (int i = stack.size() - 1; i >= 0; i--){
                        if (stack.get(i).contains(openingBracket)){
                            stack.remove(i);
                            break;
                        } else {
                            result.add(stack.get(i));
                            stack.remove(i);
                        }
                    }
                break;
            }
        }
        for (int i = stack.size() - 1; i >= 0; i--){
            result.add(stack.get(i));
        }
        return result;
    }

    private void fillHashMap(){
        priorities.put(">", "0");
        priorities.put("<", "0");
        priorities.put("==", "0");
        priorities.put(">=", "0");
        priorities.put("<=", "0");
        priorities.put("!=", "0");
        priorities.put("+", "1");
        priorities.put("-", "1");
        priorities.put("/", "2");
        priorities.put("*", "2");
        priorities.put("%", "2");
        priorities.put("^", "3");
        priorities.put("(", "4");
    }

    public ArrayList<String> divide(String input){
        input = input.replace(" ", "");
        ArrayList<String> result = new ArrayList<String>();
        String buffer = Character.toString(input.charAt(0));
        for (int i = 1; i < input.length(); i++){

            String key = Character.toString(input.charAt(i - 1));
            if (Objects.equals(chars.get(key), "ob") ||
                    Objects.equals(chars.get(key), "cb") ||
                    !Objects.equals(chars.get(key), chars.get(Character.toString(input.charAt(i)))) ||
                    Objects.equals(chars.get(key), "t")){

                result.add(buffer);
                buffer = "";
            }
            buffer += input.charAt(i);
        }
        result.add(buffer);
        return result;
    }
    public static void main(String[] args) {
        
    }
}