package nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Toast;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View v) {
        showToast("Clicked", 500);
    }

    public void onCheckedChanged(View v) {
        CheckBox checker = (CheckBox)v;
        showToast(checker.isChecked() ? "Checked" : "Unchecked", 500);
    }

    private void showToast(String text, int duration) {
        Toast toast = Toast.makeText(this.getApplicationContext(), text, duration);
        toast.show();
    }
}