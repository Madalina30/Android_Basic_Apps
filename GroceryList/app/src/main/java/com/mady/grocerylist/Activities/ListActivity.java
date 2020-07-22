package com.mady.grocerylist.Activities;

import android.app.AlertDialog;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mady.grocerylist.Data.DatabaseHandler;
import com.mady.grocerylist.Model.Grocery;
import com.mady.grocerylist.R;
import com.mady.grocerylist.UI.RecyclerViewHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewHandler recyclerViewHandler;
    private DatabaseHandler db;
    private List<Grocery> groceryList;
    private List<Grocery> groceryItems;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText addItem;
    private EditText addQtt;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        FloatingActionButton fab = findViewById(R.id.fab);

        db = new DatabaseHandler(this);
        recyclerView = findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groceryList = new ArrayList<>();
        groceryItems = new ArrayList<>();

        groceryList = db.getAllGroceries();

        for (Grocery c : groceryList) {
            Grocery grocery = new Grocery();
            grocery.setName(c.getName());
            grocery.setQuantity(c.getQuantity());
            grocery.setId(c.getId());
            grocery.setDateItemAdded(c.getDateItemAdded());

            groceryItems.add(grocery);
        }
        recyclerViewHandler = new RecyclerViewHandler(this, groceryItems);
        recyclerView.setAdapter(recyclerViewHandler);
        recyclerViewHandler.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });
    }

    public void createPopupDialog(){
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

                if (!addItem.getText().toString().isEmpty()
                        && !addQtt.getText().toString().isEmpty()) {
                    addItem(view);
                }

            }
        });
    }

    public void addItem(View view) {
        Grocery grocery = new Grocery();

        String newGrocery = addItem.getText().toString();
        String newGroceryQuantity = addQtt.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQuantity("Quantity: " + newGroceryQuantity);
//        String dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());

        grocery.setDateItemAdded("Added on: " + new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));


        //save to db
        db.addGroceryItem(grocery);
        Snackbar.make(view, "Item added!", Snackbar.LENGTH_LONG).show();
        groceryItems.add(grocery);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1000);

    }
}