package ru.nsu.koidenko;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для проверки корректности работы математических выражений.
 */
public class ExpressionTest {

    @Test
    void testToStringAndPrint() {
        Expression e = new Add(new Number(3),
                new Mul(new Number(2), new Variable("x")));
        assertEquals("(3+(2*x))", e.toString());

        Expression sub = new Sub(new Variable("y"),
                new Div(new Number(10), new Number(2)));
        assertEquals("(y-(10/2))", sub.toString());
    }

    @Test
    void testEvalWithMapAndString() {
        Expression e = new Add(new Number(3),
                new Mul(new Number(2), new Variable("x")));

        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 10);
        assertEquals(23, e.eval(vars));

        assertEquals(23, e.eval("x = 10; y = 13"));
        assertEquals(23, e.eval("  x=10 ;   "));
    }

    @Test
    void testEvalMissingVariable() {
        Expression e = new Variable("x");
        assertThrows(IllegalArgumentException.class, () -> e.eval("y = 5"));
    }

    @Test
    void testDerivative() {
        Expression e = new Add(new Number(3),
                new Mul(new Number(2), new Variable("x")));
        Expression de = e.derivative("x");
        assertEquals("(0+((0*x)+(2*1)))", de.toString());

        Expression deY = e.derivative("y");
        assertEquals("(0+((0*x)+(2*0)))", deY.toString());

        Expression divExpr = new Div(new Variable("x"), new Number(5));
        assertEquals("(((1*5)-(x*0))/(5*5))", divExpr.derivative("x").toString());

        Expression subExpr = new Sub(new Variable("x"), new Variable("x"));
        assertEquals("(1-1)", subExpr.derivative("x").toString());
    }

    @Test
    void testParseWithAndWithoutParentheses() {
        Expression parsed1 = Expression.parse("(3+(2*x))");
        assertEquals("(3+(2*x))", parsed1.toString());

        Expression parsed2 = Expression.parse("3 + 2 * x");
        assertEquals("(3+(2*x))", parsed2.toString());

        Expression parsed3 = Expression.parse("x - 10 / 2");
        assertEquals("(x-(10/2))", parsed3.toString());

        assertNull(Expression.parse(null));
    }

    @Test
    void testSimplify() {
        Expression constantExpr = new Add(new Number(3),
                new Mul(new Number(2), new Number(5)));
        assertEquals(new Number(13), constantExpr.simplify());

        Expression mulZeroLeft = new Mul(new Number(0), new Variable("x"));
        assertEquals(new Number(0), mulZeroLeft.simplify());

        Expression mulZeroRight = new Mul(new Variable("x"), new Number(0));
        assertEquals(new Number(0), mulZeroRight.simplify());

        Expression mulOneLeft = new Mul(new Number(1), new Variable("x"));
        assertEquals(new Variable("x"), mulOneLeft.simplify());

        Expression mulOneRight = new Mul(new Variable("x"), new Number(1));
        assertEquals(new Variable("x"), mulOneRight.simplify());

        Expression subSame = new Sub(
                new Add(new Variable("x"), new Number(1)),
                new Add(new Variable("x"), new Number(1))
        );
        assertEquals(new Number(0), subSame.simplify());

        Expression complex = new Add(
                new Mul(new Variable("x"), new Number(0)),
                new Mul(new Variable("y"), new Number(1))
        );
        assertEquals(new Variable("y"), complex.simplify());
    }

    @Test
    void testEqualsAndHashCode() {
        Expression num1 = new Number(5);
        Expression num2 = new Number(5);
        Expression num3 = new Number(10);

        assertEquals(num1, num2);
        assertNotEquals(num1, num3);
        assertNotEquals(num1, null);
        assertNotEquals(num1, "string");

        Expression var1 = new Variable("x");
        Expression var2 = new Variable("x");
        assertEquals(var1, var2);
        assertEquals(var1.hashCode(), var2.hashCode());

        Expression add1 = new Add(new Variable("x"), new Number(1));
        Expression add2 = new Add(new Variable("x"), new Number(1));
        assertEquals(add1, add2);
        assertEquals(add1.hashCode(), add2.hashCode());
    }

    @Test
    void testGettersAndHasVariables() {
        Number num = new Number(42);
        assertEquals(42, num.getValue());
        assertFalse(num.hasVariables());

        Variable var = new Variable("x");
        assertEquals("x", var.getName());
        assertTrue(var.hasVariables());

        Add add = new Add(num, var);
        assertEquals(num, add.getLeft());
        assertEquals(var, add.getRight());
        assertTrue(add.hasVariables());
    }
}