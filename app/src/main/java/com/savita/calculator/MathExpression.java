package com.savita.calculator;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MathExpression {
    private static String binaryOperations = "+-*/";
    private static String unaryOperations = "qs";

    public static double calculate(String expression) {
        Log.d("debug", expression);
        List<String> rpn = toRPN(expression);
        rpn.forEach(x -> Log.d("debug", x));
        return calculateRpn(rpn);
    }
    private static List<String> toRPN(String expression) {
        Log.d("debug", expression);
        ArrayList<String> rpn = new ArrayList<>(expression.length());

        char[] symbols = expression.toCharArray();

        Stack<String> operationsStack = new Stack<>();
        List<Stack<String>> operationsStackList = new ArrayList<>();
        operationsStackList.add(operationsStack);

        int level = 0;

        int i = 0;
        int length = symbols.length;

        while(i < length) {
            int j = getNumberEndingIndex(symbols, i);
            if(j >= 0) {
                rpn.add(expression.substring(i, j + 1));
                i = j + 1;
                continue;
            }

            if(binaryOperations.indexOf(symbols[i]) >= 0) {
                addOperation(rpn, operationsStackList, level, symbols[i]);
            }
            else if (unaryOperations.indexOf(symbols[i]) >= 0) {
                operationsStackList.get(level).push(String.valueOf(symbols[i]));
            }

            else if (symbols[i] == '(') {
                operationsStack = new Stack<>();
                operationsStackList.add(operationsStack);
                level++;
            }
            else if (symbols[i] == ')') {
                while (!operationsStackList.get(level).isEmpty()) {
                    rpn.add(operationsStackList.get(level).pop());
                }
                level--;
            }

            i++;
        }

        while (!operationsStackList.get(level).isEmpty()) {
            rpn.add(operationsStackList.get(level).pop());
        }

        rpn.trimToSize();
        return rpn;
    }

    private static double calculateRpn(List<String> rpn) {
        double result = 0;
        Stack<Double> calculateStack = new Stack<>();
        for (String str : rpn) {
            if (binaryOperations.indexOf(str) >= 0) {
                double number1 = Double.valueOf(calculateStack.pop());
                double number2 = Double.valueOf(calculateStack.pop());
                result = binaryCalculation(number2, number1, str);
                calculateStack.push(result);
            } else if (unaryOperations.indexOf(str) >= 0) {
                double number = Double.valueOf(calculateStack.pop());
                result = unaryCalculation(number, str);
                calculateStack.push(result);
            }
            else {
                calculateStack.push(Double.valueOf(str));
            }
        }

        Log.d("debug", calculateStack.toString());

        if(calculateStack.size() > 1) throw new ArithmeticException();

        return result;
    }

    private static double binaryCalculation(double x, double y, String operation) {
        double result = 0;

        switch(operation) {
            case "+":
                result = x + y;
                break;
            case "-":
                result = x - y;
                break;
            case "*":
                result = x * y;
                break;
            case "/":
                result = x / y;
                break;
        }

        return result;

    }

    private static double unaryCalculation(double x, String operation) {
        double result = 0;

        switch(operation) {
            case "s":
                result = x * x;
                break;
            case "q":
                result = Math.sqrt(x);
                break;
        }

        return result;

    }

    private static void addOperation(List<String> rpn, List<Stack<String>> operationsStackList, int level, char operation) {
        if (operation == '+' || operation == '-') {
            while (!operationsStackList.get(level).isEmpty()) {
                rpn.add(operationsStackList.get(level).pop());
            }
        }

        operationsStackList.get(level).push(String.valueOf(operation));
    }

    private static int getNumberEndingIndex(char[] symbols, int startIndex) {
        if(startIndex == 0 && symbols[startIndex] == '-') {
            startIndex++;
        }

        if(symbols[startIndex] < '0' || symbols[startIndex] > '9') {
            return -1;
        }

        int i = startIndex;
        while (i < symbols.length && symbols[i] >= '0' && symbols[i] <= '9') {
            i++;
        }

        if(i < symbols.length && symbols[i] == '.') {
            i++;
            while (i < symbols.length && symbols[i] >= '0' && symbols[i] <= '9') {
                i++;
            }
        }

        return i - 1;
    }
}
