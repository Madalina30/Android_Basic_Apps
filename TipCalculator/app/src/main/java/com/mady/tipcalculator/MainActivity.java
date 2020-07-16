package com.mady.tipcalculator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText enteredAmount;
    private TextView totalResultTextView;
    private TextView textViewSeekbar;
    private TextView totalBill;
    private int seekBarPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        enteredAmount = findViewById(R.id.billAmountId);
        SeekBar seekBar = findViewById(R.id.seekBar);
        Button calculateButton = findViewById(R.id.calculateButton);
        totalResultTextView = findViewById(R.id.resultID);
        textViewSeekbar = findViewById(R.id.percentage);
        totalBill = findViewById(R.id.totalBill);

        calculateButton.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewSeekbar.setText(seekBar.getProgress() + "%");
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        calculate();
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        assert inputManager != null;
        inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @SuppressLint("SetTextI18n")
    public void calculate() {
        float result;

        if (!enteredAmount.getText().toString().equals("")) {
            float enteredBillFloat = Float.parseFloat(enteredAmount.getText().toString());
            result = enteredBillFloat * seekBarPercentage / 100;
            totalResultTextView.setText("Your tip will be: $" + result + ".");
            float total = enteredBillFloat + result;
            totalBill.setText("Total bill: $" + total);
        } else {
            Toast.makeText(MainActivity.this, "Please enter a bill amount.", Toast.LENGTH_SHORT).show();
        }


    }
}