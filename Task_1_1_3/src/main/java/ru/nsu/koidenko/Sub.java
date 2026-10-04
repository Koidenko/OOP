package ru.nsu.koidenko;

import java.util.HashMap;
import java.util.Map;

public class Sub extends BinaryOperation {
    public Sub(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Expression derivative(String variableName) {
        return new Sub(left.derivative(variableName), right.derivative(variableName));
    }

    @Override
    public int eval(Map<String, Integer> variables) {
        return left.eval(variables) - right.eval(variables);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "-" + right.toString() + ")";
    }

    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        if (simplifiedLeft.equals(simplifiedRight)) {
            return new Number(0);
        }

        Expression newExpression = new Sub(simplifiedLeft, simplifiedRight);
        if (!newExpression.hasVariables()) {
            return new Number(newExpression.eval(new HashMap<>()));
        }
        return newExpression;
    }
}