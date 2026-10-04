package ru.nsu.koidenko;

import java.util.Objects;

/**
 * Базовый класс для бинарных операций.
 */
public abstract class BinaryOperation extends Expression {

    protected final Expression left;
    protected final Expression right;

    public BinaryOperation(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public boolean hasVariables() {
        return left.hasVariables() || right.hasVariables();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BinaryOperation other = (BinaryOperation) obj;
        return Objects.equals(left, other.left) && Objects.equals(right, other.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}