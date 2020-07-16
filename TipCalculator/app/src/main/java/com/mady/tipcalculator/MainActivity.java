package com.mady.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText enteredAmount;
    private SeekBar seekBar;
    private Button calculateButton;
    private TextView totalResultTextView;
    private TextView textViewSeekbar;
    private TextView totalBill;
    private  int seekBarPercentage;
    private float enteredBillFloat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        enteredAmount = findViewById(R.id.billAmountId);
        seekBar = findViewById(R.id.seekBar);
        calculateButton = findViewById(R.id.calculateButton);
        totalResultTextView = findViewById(R.id.resultID);
        textViewSeekbar = findViewById(R.id.percentage);
        totalBill = findViewById(R.id.totalBill);

        calculateButton.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewSeekbar.setText(String.valueOf(seekBar.getProgress()) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarPercentage = seekBar.getProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        calculate();
    }
    public void calculate(){
        float result = 0.0f;

        if (!enteredAmount.getText().toString().equals("")){
            enteredBillFloat = Float.parseFloat(enteredAmount.getText().toString());
            result = enteredBillFloat * seekBarPercentage / 100;
            totalResultTextView.setText("Your tip will be: $" + String.valueOf(result) + ".");
            float total = enteredBillFloat + result;
            totalBill.setText("Total bill: $" + String.valueOf(total));
        } else {
            Toast.makeText(MainActivity.this, "Please enter a bill amount.", Toast.LENGTH_SHORT).show();
        }


    }
}