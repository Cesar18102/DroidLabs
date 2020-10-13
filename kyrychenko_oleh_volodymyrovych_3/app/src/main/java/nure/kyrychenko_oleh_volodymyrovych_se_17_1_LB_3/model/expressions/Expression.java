package nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.expressions;

public interface Expression {
    Expression simplify();
    boolean canSimplify();
    String stringify();
}
