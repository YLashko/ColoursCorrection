package yl.main;

import java.util.HashMap;
import java.util.ArrayList;
import java.lang.Double;
import java.lang.Math;
import java.lang.Character;
public class SmallMathCompiler {

    HashMap<String, Double> variables = new HashMap<>();
    HashMap<String, String> chars = Options.chars;
    SmallMathCompiler(){

    }

    public void setVariable(String name, Double value) {
        variables.put(name, value);
    }

    public double getVariable(String name) {
        return variables.get(name);
    }

    public double processTernary(TernaryFunction tf){
        if (tf.content.size() == 0){
            return processRPN(tf.rpnFunc);
        }
        if (processTernary(tf.content.get(0)) > 0){
            return processTernary(tf.content.get(1));
        } else {
            return processTernary(tf.content.get(2));
        }
    }

    public double processRPN(ArrayList<String> rpnString) {
        ArrayList<Double> stack = new ArrayList<>();
        for (String el: rpnString){
            switch (chars.get(Character.toString(el.charAt(0)))) {

                case "n":
                    stack.add(Double.parseDouble(el));
                break;

                case "o":
                    double result = 0d;
                    double var1 = stack.get(stack.size() - 2);
                    double var2 = stack.get(stack.size() - 1); 
                    switch (el){
                        case ">":
                            result = (var1 > var2)? 1: -1;
                        break;
                        case "<":
                            result = (var1 < var2)? 1: -1;
                        break;
                        case ">=":
                            result = (var1 >= var2)? 1: -1;
                        break;
                        case "<=":
                            result = (var1 <= var2)? 1: -1;
                        break;
                        case "==":
                            result = (var1 == var2)? 1: -1;
                        break;
                        case "!=":
                            result = (var1 != var2)? 1: -1;
                        break;
                        case "+":
                            result = var1 + var2;
                        break;
                        case "-":
                            result = var1 - var2;
                        break;
                        case "/":
                            result = var1 / var2; 
                        break;
                        case "*":
                            result = var1 * var2;
                        break;
                        case "%":
                            result = var1 % var2;
                        break;
                        case "^":
                            result = Math.pow(var1, var2);
                        break;
                    }
                    stack.remove(stack.size() - 1); 
                    stack.remove(stack.size() - 1); 
                    stack.add(result);
                break;

                case "v":
                    stack.add(variables.get(el));
                break;
            }
        }
        return stack.get(0);
    }


}
