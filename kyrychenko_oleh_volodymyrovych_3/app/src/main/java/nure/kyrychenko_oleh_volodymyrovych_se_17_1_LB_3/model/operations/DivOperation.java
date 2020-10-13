package nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.operations;

import nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.expressions.Expression;
import nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.expressions.NumberExpression;

public class DivOperation extends Operation {
    @Override
    protected NumberExpression apply(int left, int right) {
        if (right == 0) {
            throw new ArithmeticException("На ноль делить нельзя!");
        }

        return new NumberExpression(left / right);
    }

    @Override
    protected NumberExpression apply(float left, float right) {
        if (right == 0) {
            throw new ArithmeticException("На ноль делить нельзя!");
        }

        return new NumberExpression(left / right);
    }

    @Override
    public boolean isAlwaysFloatResult() { return true; }

    @Override
    public int getPriority() { return 2; }

    @Override
    public String stringify(Expression right) {
        return "/";
    }
}
