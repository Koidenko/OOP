package ru.nsu.koidenko;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Абстрактный базовый класс, представляющий математическое выражение.
 * Предоставляет функциональность для дифференцирования, вычисления,
 * печатного отображения, парсинга и упрощения выражений.
 */
public abstract class Expression {
    /**
     * Вычисляет символьную производную выражения по заданной переменной.
     *
     * @param variableName имя переменной, по которой выполняется дифференцирование
     * @return новое математическое выражение, представляющее производную
     */
    public abstract Expression derivative(String variableName);

    /**
     * Вычисляет числовое значение выражения на основе переданных значений переменных.
     *
     * @param variables карта, содержащая имена переменных и их числовые значения
     * @return результат вычисления выражения
     * @throws IllegalArgumentException если значение какой-либо переменной не найдено
     */
    public abstract int eval(Map<String, Integer> variables);

    /**
     * Вычисляет числовое значение выражения на основе строки с означиванием переменных.
     *
     * @param assignments строка вида "x = 10; y = 13", задающая значения переменных
     * @return результат вычисления выражения
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

    public abstract String toString();

    /**
     * Выводит строковое представление выражения в указанный поток.
     *
     * @param stream поток вывода (например, System.out)
     */
    public void print(PrintStream stream) {
        stream.print(this.toString());
    }

    /**
     * Выводит строковое представление выражения в стандартный поток вывода (консоль).
     */
    public void print() {
        print(System.out);
    }

    /**
     * Проверяет, содержит ли выражение хотя бы одну переменную.
     *
     * @return true, если в выражении есть переменные, иначе false
     */
    public abstract boolean hasVariables();

    /**
     * Выполняет упрощение математического выражения по заданным правилам.
     *
     * @return новое упрощенное выражение
     */
    public abstract Expression simplify();

    /**
     * Создает объект математического выражения из его строкового представления.
     * Поддерживает строки как со скобками, так и без них с учетом приоритета операций.
     *
     * @param input строковое выражение (например, "(3+(2*x))" или "3 + 2 * x")
     * @return распарсенное математическое выражение или null, если строка пуста
     */
    public static Expression parse(String input) {
        if (input == null) {
            return null;
        }
        String sanitized = input.replaceAll("\\s+", "");
        return parseExpression(sanitized);
    }

    private static Expression parseExpression(String input) {
        if (input.startsWith("(") && input.endsWith(")") && isBalanced(input.substring(1, input.length() - 1))) {
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
            Expression leftExpression = parseExpression(leftPart);
            Expression rightExpression = parseExpression(rightPart);
            char operator = input.charAt(mainOp);

            switch (operator) {
                case '+':
                    return new Add(leftExpression, rightExpression);
                case '-':
                    return new Sub(leftExpression, rightExpression);
                case '*':
                    return new Mul(leftExpression, rightExpression);
                case '/':
                    return new Div(leftExpression, rightExpression);
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