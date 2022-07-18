package com.sap.cc.fortunecookies;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class ShuntingYard {

    public static void main(String[] args) {
        String expression = "5+2*3+(1-5)*2";
        double value = getValue(expression);
        System.out.printf("Result is %s%n", value);

    }

    private static double getValue(String expression) {
        Queue<Character> postFix = getPostFix(expression);
        return processPostFix(postFix);
    }

    private static double processPostFix(Queue<Character> postFix) {
        Stack<Integer> finalStack = new Stack<>();
        for (Character c : postFix) {
            if(Character.isDigit(c)){
                finalStack.push(c -'0');//Convert car to number
            } else {
                int lastValue = finalStack.pop();
                int secondLast = finalStack.pop();
                switch(c)
                {
                    case '+':
                        finalStack.push(lastValue+secondLast);
                        break;

                    case '-':
                        finalStack.push(secondLast - lastValue);
                        break;

                    case '/':
                        finalStack.push(secondLast/lastValue);
                        break;

                    case '*':
                        finalStack.push(secondLast * lastValue);
                        break;
                }
            }
            System.out.print(c);
        }
        return finalStack.pop();
    }

    private static Queue<Character> getPostFix(String expression) {
        Queue<Character> operand = new ArrayDeque<>();
        Stack<Character> operator = new Stack<>();
        for (int i = 0; i < expression.length(); i++) {
            char currentValue = expression.charAt(i);
            if (Character.isDigit(currentValue)) {
                operand.add(currentValue);
            } else {
                if (ops.containsKey(currentValue)) {
                    int prec = ops.get(currentValue).precendence;
                    if (operator.isEmpty()) {
                        operator.push(currentValue);
                    } else {
                        char oldValue = operator.pop();
                        if (ops.containsKey(oldValue)) {
                            if (ops.get(oldValue).precendence <= prec) {
                                operator.push(oldValue);
                                operator.push(currentValue);
                            } else {
                                operand.add(oldValue);
                                popAll(operand, operator);
                                operator.push(currentValue);
                            }
                        } else {
                            operator.push(oldValue);
                            operator.push(currentValue);
                        }

                    }

                } else if (currentValue == '(') {
                    operator.push('(');
                } else if (currentValue == ')') {
                    char popedValue;
                    popedValue = operator.pop();
                    while (popedValue != '(') {
                        operand.add(popedValue);
                        popedValue = operator.pop();
                    }
                }
            }

        }
        popAll(operand, operator);
        return operand;
    }

    private static void popAll(Queue<Character> operand, Stack<Character> operator) {
        while (!operator.isEmpty()) {
            operand.add(operator.pop());
        }
    }


    private enum Operator {
        ADD(1), SUB(1), MULT(2), DIV(2);
        final int precendence;

        Operator(int p) {
            precendence = p;
        }
    }

    private static Map<Character, Operator> ops = new HashMap() {{
        put('+', Operator.ADD);
        put('-', Operator.SUB);
        put('*', Operator.MULT);
        put('/', Operator.DIV);
    }};
}
