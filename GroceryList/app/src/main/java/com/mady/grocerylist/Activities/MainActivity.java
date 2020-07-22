package com.mady.grocerylist.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mady.grocerylist.Data.DatabaseHandler;
import com.mady.grocerylist.Model.Grocery;
import com.mady.grocerylist.R;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AlertDialog dialog;
    private EditText addItem;
    private EditText addQtt;
    private DatabaseHandler db;
    protected Button saveButton;
    protected AlertDialog.Builder dialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);

        byPassActivity();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });
    }

    private void createPopupDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        addItem = view.findViewById(R.id.enterItem);
        addQtt = view.findViewById(R.id.enterQtt);
        saveButton = view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo: save to db
                //Todo: go to next screen

                if (!addItem.getText().toString().isEmpty()
                        && !addQtt.getText().toString().isEmpty()) {
                    saveGroceryToDB(view);
                }

            }
        });
    }

    private void saveGroceryToDB(View view) {

        Grocery grocery = new Grocery();

        String newGrocery = addItem.getText().toString();
        String newGroceryQuantity = addQtt.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQuantity("Quantity: " + newGroceryQuantity);
        grocery.setDateItemAdded("Added on: " + new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));

        //save to db
        db.addGroceryItem(grocery);
        Snackbar.make(view, "Item added!", Snackbar.LENGTH_LONG).show();

//        Log.d("Item added ID: ", String.valueOf(db.getGroceriesCount()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //go to next activity
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                finish();
            }
        }, 1000);

    }

    public void byPassActivity() {
        //checks if database is empty; if not, it will go straight to listActivity
        if (db.getGroceriesCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }
    }
}