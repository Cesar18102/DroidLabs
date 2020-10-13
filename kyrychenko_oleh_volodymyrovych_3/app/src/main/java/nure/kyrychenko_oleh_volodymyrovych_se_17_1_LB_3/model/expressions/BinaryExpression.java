package nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.expressions;

import nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.operations.Operation;

public class BinaryExpression implements Expression {
    private Expression left;
    private Expression right;
    private Operation operation;

    public Expression getLeft() { return left; }
    public void setLeft(Expression left) { this.left = left; }

    public Expression getRight() { return right; }
    public void setRight(Expression right) { this.right = right; }

    public Operation getOperation() { return operation; }
    public void setOperation(Operation operation) { this.operation = operation; }

    @Override
    public Expression simplify() {
        if(left == null && right == null) {
            return new NumberExpression(0);
        }

        if(left != null && right == null) {
            if(operation == null) {
                return left.simplify();
            }

            return operation.apply((NumberExpression) left, new NumberExpression(0));
        }

        if(!left.canSimplify() && !right.canSimplify()) {
            return operation.apply((NumberExpression) left, (NumberExpression) right);
        } else {
            if(left.canSimplify()) {
                left = left.simplify();
            } else if(right.canSimplify()) {
                right = right.simplify();
            }
            return this;
        }
    }

    @Override
    public boolean canSimplify() { return true; }

    @Override
    public String stringify() {
        StringBuilder builder = new StringBuilder();

        if(left != null) {
            builder.append(left.stringify());
        }

        if(operation != null) {
            String operationString = operation.stringify(right);
            if(!operationString.isEmpty()) {
                builder.append(" ").append(operationString);
            }
        }

        if(right != null) {
            builder.append(" ").append(right.stringify());
        }

        return builder.toString();
    }
}
