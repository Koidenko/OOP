package ru.nsu.koidenko;

import java.util.Map;
import java.util.Objects;

/**
 * Числовая константа.
 */
public class Number extends Expression {

    private final int value;

    public Number(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public Expression derivative(String variableName) {
        return new Number(0);
    }

    @Override
    public int eval(Map<String, Integer> variables) {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean hasVariables() {
        return false;
    }

    @Override
    public Expression simplify() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Number number = (Number) obj;
        return value == number.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}