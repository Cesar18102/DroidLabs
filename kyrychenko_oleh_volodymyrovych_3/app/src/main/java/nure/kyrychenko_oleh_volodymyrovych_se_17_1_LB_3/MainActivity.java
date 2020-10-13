package nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.expressions.BinaryExpression;
import nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.expressions.Expression;
import nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.expressions.NumberExpression;
import nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.operations.DivOperation;
import nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.operations.MultOperation;
import nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.operations.Operation;
import nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_3.model.operations.PlusOperation;

public class MainActivity extends AppCompatActivity {
    private BinaryExpression root;
    private BinaryExpression pointer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reset();
    }

    private NumberExpression getCurrentNumberExpression() {
        if(pointer.getOperation() == null) {
            return (NumberExpression) pointer.getLeft();
        }
        return  (NumberExpression) pointer.getRight();
    }

    private void pushOperation(Operation operation) {
        if(pointer.getOperation() == null) {
            pointer.setOperation(operation);
            pointer.setRight(new NumberExpression(0));
        } else if(pointer.getOperation().getPriority() > operation.getPriority()) {
            BinaryExpression expression = new BinaryExpression();

            expression.setLeft(root);
            expression.setOperation(operation);
            expression.setRight(new NumberExpression(0));

            root = pointer = expression;
        } else {
            BinaryExpression expression = new BinaryExpression();

            expression.setLeft(pointer.getRight());
            expression.setOperation(operation);
            expression.setRight(new NumberExpression(0));

            pointer.setRight(expression);
            pointer = expression;
        }
    }

    public void onDigitClick(View v) {
        String buttonText = ((Button)v).getText().toString();
        int buttonDigit = Integer.parseInt(buttonText);

        getCurrentNumberExpression().pushDigit(buttonDigit);
        updateResultView();
    }

    public void onCommaClick(View v) {
        getCurrentNumberExpression().setHasComma(true);
        updateResultView();
    }

    public void onPlusClick(View v) {
        pushOperation(new PlusOperation());
        updateResultView();
    }

    public void onMinusClick(View v) {
        pushOperation(new PlusOperation());
        ((NumberExpression)pointer.getRight()).setHasMinus(true);

        updateResultView();
    }

    public void onMultClick(View v) {
        pushOperation(new MultOperation());
        updateResultView();
    }

    public void onDivClick(View v) {
        pushOperation(new DivOperation());
        updateResultView();
    }

    public void onEqualsClick(View v) {
        try {
            Expression expression = root;
            StringBuilder builder = new StringBuilder(expression.stringify());

            while (expression.canSimplify()) {
                expression = expression.simplify();
            }

            builder.append(" = ").append(expression.stringify());

            reset(expression);
            ((EditText) findViewById(R.id.result)).setText(builder.toString());
        } catch (ArithmeticException ex) {
            reset();
            ((EditText) findViewById(R.id.result)).setText(ex.getMessage());
        }
    }

    public void onClearClick(View v) {
        reset();
        updateResultView();
    }

    private void reset() {
        reset(new NumberExpression(0));
    }

    private void reset(Expression value) {
        pointer = root = new BinaryExpression();
        pointer.setLeft(value);
    }

    private void updateResultView() {
        ((EditText)findViewById(R.id.result)).setText(root.stringify());
    }
}