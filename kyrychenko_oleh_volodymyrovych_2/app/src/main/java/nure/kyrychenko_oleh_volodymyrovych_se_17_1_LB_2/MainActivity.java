package nure.kyrychenko_oleh_volodymyrovych_se_17_1_LB_2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;

import android.os.Bundle;

import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.color).setBackgroundColor(Color.rgb(0, 0, 0));
        ColorChangeListener changeColorListener = new ColorChangeListener();

        SeekBar red = (SeekBar)findViewById(R.id.red);
        red.setOnSeekBarChangeListener(changeColorListener);

        SeekBar green = (SeekBar)findViewById(R.id.green);
        green.setOnSeekBarChangeListener(changeColorListener);

        SeekBar blue = (SeekBar)findViewById(R.id.blue);
        blue.setOnSeekBarChangeListener(changeColorListener);
    }

    private int getCurrentColor() {
        return Color.rgb(
                ((SeekBar)findViewById(R.id.red)).getProgress(),
                ((SeekBar)findViewById(R.id.green)).getProgress(),
                ((SeekBar)findViewById(R.id.blue)).getProgress()
        );
    }

    private class ColorChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            findViewById(R.id.color).setBackgroundColor(getCurrentColor());
        }

        @Override public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override public void onStopTrackingTouch(SeekBar seekBar) { }
    }
}