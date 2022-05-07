package yl.main;

import java.util.ArrayList;

public class TernaryFunction {
    public String func;
    public ArrayList<String> rpnFunc = new ArrayList<String>();
    public ArrayList<TernaryFunction> content = new ArrayList<TernaryFunction>();

    TernaryFunction(String func){
        this.func = func;
    }

    public void compile(){
        divide();
        setrpnFunc();
    }

    private void divide(){
        String condition = "";
        String tr = "";
        String fl = "";
        if (func.contains("?")){
            int qCounter = 0;
            int point = 0;
            for (int i = 0; i < func.length(); i++){

                if (func.charAt(i) == '?'){
                    qCounter++;
                    if (condition == ""){
                        condition = func.substring(0, i);
                        point = i;
                    }

                } else if (func.charAt(i) == ':'){
                    qCounter--;
                    if (qCounter == 0){
                        tr = func.substring(point + 1, i);
                        fl = func.substring(i + 1);
                        break;
                    }
                }
            }
            content.add(new TernaryFunction(condition));
            TernaryFunction trTern = new TernaryFunction(tr);
            if (tr.contains("?")){ trTern.divide(); }
            content.add(trTern);
            TernaryFunction flTern = new TernaryFunction(fl);
            if (fl.contains("?")){ flTern.divide(); }
            content.add(flTern);
        }
    }

    public void setrpnFunc(){
        RPNConverter rpnc = new RPNConverter();
        if (content.isEmpty()){ 
            rpnFunc = rpnc.toRPN(func);
        } else {
            content.get(0).setrpnFunc();
            content.get(1).setrpnFunc();
            content.get(2).setrpnFunc();
        }
    }

    public String getContent(){ // for tests only
        if (content.size() == 0){ return func; }
        String ret = "";
        ret += content.get(0).getContent() + " ? ";
        ret += content.get(1).getContent() + " : ";
        ret += content.get(2).getContent();
        return ret;
    }

    public String getContentRPN(){ // for tests only
        if (content.size() == 0){ return mergeRPN(); }
        String ret = "";
        ret += content.get(0).getContentRPN() + " ? ";
        ret += content.get(1).getContentRPN() + " : ";
        ret += content.get(2).getContentRPN();
        return ret;
    }

    private String mergeRPN(){ // for tests only
        String ret = "";
        for (String el: rpnFunc){ ret += el + " "; }
        return ret;
    }

}
