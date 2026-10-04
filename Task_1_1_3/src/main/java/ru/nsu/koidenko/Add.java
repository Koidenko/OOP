package ru.nsu.koidenko;

import java.util.HashMap;
import java.util.Map;

/**
 * Операция сложения.
 */
public class Add extends BinaryOperation {

    public Add(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Expression derivative(String variableName) {
        return new Add(left.derivative(variableName), right.derivative(variableName));
    }

    @Override
    public int eval(Map<String, Integer> variables) {
        return left.eval(variables) + right.eval(variables);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "+" + right.toString() + ")";
    }

    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        if (simplifiedLeft.equals(new Number(0))) {
            return simplifiedRight;
        }
        if (simplifiedRight.equals(new Number(0))) {
            return simplifiedLeft;
        }

        Expression newExpression = new Add(simplifiedLeft, simplifiedRight);
        if (!newExpression.hasVariables()) {
            return new Number(newExpression.eval(new HashMap<>()));
        }
        return newExpression;
    }
}