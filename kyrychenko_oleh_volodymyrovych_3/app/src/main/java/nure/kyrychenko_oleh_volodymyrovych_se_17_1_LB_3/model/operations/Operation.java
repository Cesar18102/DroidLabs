package nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.operations;

import nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.expressions.Expression;
import nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.expressions.NumberExpression;

public abstract class Operation {
     public NumberExpression apply(NumberExpression left, NumberExpression right) {
          if(left.hasComma() || right.hasComma() || isAlwaysFloatResult()) {
               return apply(left.getFullFloatValue(), right.getFullFloatValue());
          }

          return apply(left.getIntValue(), right.getIntValue());
     }

     protected abstract NumberExpression apply(int left, int right);
     protected abstract NumberExpression apply(float left, float right);

     public boolean isAlwaysFloatResult() { return false; }
     public abstract int getPriority();
     public abstract String stringify(Expression rigth);
}
