package nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.operations;

import nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.expressions.BinaryExpression;
import nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.expressions.Expression;
import nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.expressions.NumberExpression;

public class PlusOperation extends Operation {
    @Override
    protected NumberExpression apply(int left, int right) { return new NumberExpression(left + right); }

    @Override
    protected NumberExpression apply(float left, float right) { return new NumberExpression(left + right); }

    @Override
    public int getPriority() { return 1; }

    @Override
    public String stringify(Expression right) {
        return findAffector(right).hasMinus() ? "" : "+";
    }

    private NumberExpression findAffector(Expression expression) {
        if(expression.getClass().equals(NumberExpression.class)) {
            return (NumberExpression) expression;
        }

        return findAffector(((BinaryExpression)expression).getLeft());
    }
}
