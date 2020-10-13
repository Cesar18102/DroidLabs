package nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.expressions;

public class NumberExpression implements Expression {
    private int intValue;
    private boolean comma;
    private float floatValue;
    private int floatDigitsCount;
    private boolean minus;

    public int getIntValue() { return minus ? -intValue : intValue; }
    public float getFloatValue() { return minus ? -floatValue : floatValue; }
    public float getFullFloatValue() { return getIntValue() + getFloatValue(); }

    public boolean hasComma() { return comma; }
    public void setHasComma(boolean comma) { this.comma = comma; }

    public boolean hasMinus() { return minus; }
    public void setHasMinus(boolean minus) { this.minus = minus; }

    public void pushDigit(int digit) {
        if(comma) {
            floatDigitsCount++;
            floatValue += digit / Math.pow(10, floatDigitsCount);
        } else {
            intValue *= 10;
            intValue += digit;
        }
    }

    public NumberExpression(int intValue) {
        this.intValue = Math.abs(intValue);

        if(intValue < 0)
            minus = true;
    }

    public NumberExpression(float fullFloatValue) {
        float temp = Math.abs(fullFloatValue);

        intValue = (int)temp;
        floatValue = temp - intValue;

        comma = true;
        if(fullFloatValue < 0)
            minus = true;
    }

    @Override
    public Expression simplify() {
        return this;
    }

    @Override
    public boolean canSimplify() { return false; }

    @Override
    public String stringify() {
        String numberText = comma ? Float.toString(intValue + floatValue) : Integer.toString(intValue);
        return (minus ? "- " : "") + numberText;
    }
}
