package ru.nsu.koidenko;

import java.util.HashMap;
import java.util.Map;

public class Div extends BinaryOperation {
    public Div(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Expression derivative(String variableName) {
        return new Div(
                new Sub(
                        new Mul(left.derivative(variableName), right),
                        new Mul(left, right.derivative(variableName))
                ),
                new Mul(right, right)
        );
    }

    @Override
    public int eval(Map<String, Integer> variables) {
        return left.eval(variables) / right.eval(variables);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "/" + right.toString() + ")";
    }

    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        Expression newExpression = new Div(simplifiedLeft, simplifiedRight);
        if (!newExpression.hasVariables()) {
            return new Number(newExpression.eval(new HashMap<>()));
        }
        return newExpression;
    }
}