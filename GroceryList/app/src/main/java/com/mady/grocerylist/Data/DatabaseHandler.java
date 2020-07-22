package com.mady.grocerylist.Data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.mady.grocerylist.Model.Grocery;
import com.mady.grocerylist.Utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_GROCERY_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_GROCERY_ITEM + " TEXT,"
                + Constants.KEY_QTT_NUMBER + " TEXT,"
                + Constants.KEY_DATE_NAME + " LONG);";
        sqLiteDatabase.execSQL(CREATE_GROCERY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }

    //CRUD OPERATIONS: create, read, update, delete

    //add grocery item
    public void addGroceryItem(Grocery grocery) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_GROCERY_ITEM, grocery.getName());
        contentValues.put(Constants.KEY_QTT_NUMBER, grocery.getQuantity());
        contentValues.put(Constants.KEY_DATE_NAME, grocery.getDateItemAdded());

        sqLiteDatabase.insert(Constants.TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    //get a grocery item
    public Grocery getGrocery(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.query(
                Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID, Constants.KEY_GROCERY_ITEM, Constants.KEY_QTT_NUMBER, Constants.KEY_DATE_NAME},
                Constants.KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        Grocery grocery = new Grocery();
        assert cursor != null;
        grocery.setId(Integer.parseInt(cursor.getString(0)));
        grocery.setName(cursor.getString(1));
        grocery.setQuantity(cursor.getString(2));
        grocery.setDateItemAdded(cursor.getString(3));


        return grocery;
    }

    //get all groceries
    public List<Grocery> getAllGroceries() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<Grocery> groceryList = new ArrayList<>();

        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID, Constants.KEY_GROCERY_ITEM, Constants.KEY_QTT_NUMBER, Constants.KEY_DATE_NAME},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                Grocery grocery = new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(0)));
                grocery.setName(cursor.getString(1));
                grocery.setQuantity(cursor.getString(2));
                grocery.setDateItemAdded(cursor.getString(3));




                //add to the grocery list
                groceryList.add(grocery);

            } while (cursor.moveToNext());
        }

        return groceryList;
    }

    //update grocery
    public int updateGrocery(Grocery grocery) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_GROCERY_ITEM, grocery.getName());
        contentValues.put(Constants.KEY_QTT_NUMBER, grocery.getQuantity());
        contentValues.put(Constants.KEY_DATE_NAME, grocery.getDateItemAdded());

        return sqLiteDatabase.update(Constants.TABLE_NAME, contentValues, Constants.KEY_ID + "=?", new String[]{String.valueOf(grocery.getId())});
    }

    //delete grocery
    public void deleteGrocery(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(Constants.TABLE_NAME, Constants.KEY_ID + " =?", new String[]{String.valueOf(id)});

        sqLiteDatabase.close();

    }

    //count of groceries
    public int getGroceriesCount() {
        String countGroceries = "SELECT * FROM " + Constants.TABLE_NAME;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(countGroceries, null);

        return cursor.getCount();
    }
}
