package ru.nsu.koidenko;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Базовый абстрактный класс для математических выражений.
 */
public abstract class Expression {

    /**
     * Вычисляет производную выражения по переменной.
     *
     * @param variableName имя переменной
     * @return выражение производной
     */
    public abstract Expression derivative(String variableName);

    /**
     * Вычисляет значение выражения с заданными переменными.
     *
     * @param variables карта значений переменных
     * @return результат вычисления
     */
    public abstract int eval(Map<String, Integer> variables);

    /**
     * Вычисляет значение выражения по строке присваиваний.
     *
     * @param assignments строка вида "x = 10; y = 13"
     * @return результат вычисления
     */
    public int eval(String assignments) {
        Map<String, Integer> variables = new HashMap<>();
        if (assignments != null && !assignments.trim().isEmpty()) {
            String[] parts = assignments.split(";");
            for (String part : parts) {
                String token = part.trim();
                if (!token.isEmpty()) {
                    String[] keyValue = token.split("=");
                    if (keyValue.length == 2) {
                        variables.put(keyValue[0].trim(), Integer.parseInt(keyValue[1].trim()));
                    }
                }
            }
        }
        return eval(variables);
    }

    @Override
    public abstract String toString();

    public void print(PrintStream stream) {
        stream.print(this.toString());
    }

    public void print() {
        print(System.out);
    }

    /**
     * Проверяет наличие переменных в выражении.
     *
     * @return true, если есть переменные
     */
    public abstract boolean hasVariables();

    /**
     * Упрощает выражение по математическим правилам.
     *
     * @return упрощенное выражение
     */
    public abstract Expression simplify();

    /**
     * Разбирает строку в математическое выражение.
     *
     * @param input входная строка
     * @return распарсенное выражение
     */
    public static Expression parse(String input) {
        if (input == null) {
            return null;
        }
        String sanitized = input.replaceAll("\\s+", "");
        return parseExpression(sanitized);
    }

    private static Expression parseExpression(String input) {
        if (input.startsWith("(") && input.endsWith(")")
                && isBalanced(input.substring(1, input.length() - 1))) {
            return parseExpression(input.substring(1, input.length() - 1));
        }

        int mainOp = -1;
        int minPriority = Integer.MAX_VALUE;
        int depth = 0;

        for (int i = input.length() - 1; i >= 0; i--) {
            char currentChar = input.charAt(i);
            if (currentChar == ')') {
                depth++;
            } else if (currentChar == '(') {
                depth--;
            } else if (depth == 0) {
                int priority = getOperatorPriority(currentChar);
                if (priority != -1 && priority < minPriority) {
                    minPriority = priority;
                    mainOp = i;
                }
            }
        }

        if (mainOp != -1) {
            String leftPart = input.substring(0, mainOp);
            String rightPart = input.substring(mainOp + 1);
            Expression leftExpr = parseExpression(leftPart);
            Expression rightExpr = parseExpression(rightPart);
            char operator = input.charAt(mainOp);

            switch (operator) {
                case '+':
                    return new Add(leftExpr, rightExpr);
                case '-':
                    return new Sub(leftExpr, rightExpr);
                case '*':
                    return new Mul(leftExpr, rightExpr);
                case '/':
                    return new Div(leftExpr, rightExpr);
                default:
                    break;
            }
        }

        if (input.matches("-?\\d+")) {
            return new Number(Integer.parseInt(input));
        }

        return new Variable(input);
    }

    private static boolean isBalanced(String input) {
        int depth = 0;
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (currentChar == '(') {
                depth++;
            } else if (currentChar == ')') {
                depth--;
            }
            if (depth < 0) {
                return false;
            }
        }
        return depth == 0;
    }

    private static int getOperatorPriority(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }
}