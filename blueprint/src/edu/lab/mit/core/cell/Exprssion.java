package edu.lab.mit.core.cell;

import java.util.Stack;

/**
 * <p>Project: blueprint</p>
 * <p>Summary: Exprssion</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Institution: MIT LIB Co., Ltd.</p>
 *
 * @author chao.deng
 * @version 1.0
 * @since 4/22/2016
 */
public class Exprssion {

    private static final String SPACE = " ";

    private static int rank(String op) {
        switch (op) {
            case "*":
            case "/":
                return 2;
            case "+":
            case "-":
                return 1;
            default:
                return -1;
        }
    }

    public static String toPostfix(String expr) {
        StringBuilder result = new StringBuilder();
        Stack<String> operators = new Stack<>();
        for (String token : expr.split("\\s+")) {
            if (rank(token) > 0) {
                while (!operators.isEmpty() && rank(operators.peek()) >= rank(token)) {
                    result.append(operators.pop()).append(SPACE);
                }
                operators.push(token);
            } else {
                result.append(token).append(SPACE);
            }
        }
        while (!operators.isEmpty()) {
            result.append(operators.pop()).append(SPACE);
        }
        return result.toString();
    }
}
