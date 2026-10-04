package ru.nsu.koidenko;

import java.util.Objects;

/**
 * Абстрактный класс для бинарных математических операций, содержащих левый и правый операнды.
 */
public abstract class BinaryOperation extends Expression {
    /** Левый операнд бинарной операции. */
    protected final Expression left;
    /** Правый операнд бинарной операции. */
    protected final Expression right;

    /**
     * Конструктор для создания бинарной операции.
     *
     * @param left левое подвыражение
     * @param right правое подвыражение
     */
    public BinaryOperation(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Возвращает левый операнд.
     *
     * @return левое подвыражение
     */
    public Expression getLeft() {
        return left;
    }

    /**
     * Возвращает правый операнд.
     *
     * @return правое подвыражение
     */
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