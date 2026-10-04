package ru.nsu.koidenko;

import java.util.Map;
import java.util.Objects;

/**
 * Класс, представляющий переменная в математическом выражении.
 */
public class Variable extends Expression {
    private final String name;

    /**
     * Конструктор для создания переменной.
     *
     * @param name имя переменной (может быть многобуквенным)
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Возвращает имя переменной.
     *
     * @return имя переменной
     */
    public String getName() {
        return name;
    }

    @Override
    public Expression derivative(String variableName) {
        if (this.name.equals(variableName)) {
            return new Number(1);
        }
        return new Number(0);
    }

    @Override
    public int eval(Map<String, Integer> variables) {
        if (!variables.containsKey(name)) {
            throw new IllegalArgumentException("Variable not found: " + name);
        }
        return variables.get(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean hasVariables() {
        return true;
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
        Variable variable = (Variable) obj;
        return Objects.equals(name, variable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}