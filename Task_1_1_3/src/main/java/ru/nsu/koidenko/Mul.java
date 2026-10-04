package ru.nsu.koidenko;

import java.util.HashMap;
import java.util.Map;

public class Mul extends BinaryOperation {
    public Mul(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Expression derivative(String variableName) {
        return new Add(
                new Mul(left.derivative(variableName), right),
                new Mul(left, right.derivative(variableName))
        );
    }

    @Override
    public int eval(Map<String, Integer> variables) {
        return left.eval(variables) * right.eval(variables);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "*" + right.toString() + ")";
    }

    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        if (simplifiedLeft.equals(new Number(0)) || simplifiedRight.equals(new Number(0))) {
            return new Number(0);
        }
        if (simplifiedLeft.equals(new Number(1))) {
            return simplifiedRight;
        }
        if (simplifiedRight.equals(new Number(1))) {
            return simplifiedLeft;
        }

        Expression newExpression = new Mul(simplifiedLeft, simplifiedRight);
        if (!newExpression.hasVariables()) {
            return new Number(newExpression.eval(new HashMap<>()));
        }
        return newExpression;
    }
}