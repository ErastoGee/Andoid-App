package com.example.babyneeds.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.babyneeds.model.Item;
import com.example.babyneeds.util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.babyneeds.util.Constants.KEY_COLOR;
import static com.example.babyneeds.util.Constants.TABLE_NAME;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_BABY_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_BABY_ITEM + " TEXT,"
                + Constants.KEY_COLOR + " TEXT,"
                + Constants.KEY_QUANTITY_NUMBER+ " INTEGER,"
                + Constants.KEY_ITEM_SIZE + " INTEGER,"
                + Constants.KEY_DATE_NAME + " LONG"+")";
        db.execSQL(CREATE_BABY_TABLE); //creating our table

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf("DROP TABLE IF EXISTS");
        db.execSQL(DROP_TABLE, new String[]{TABLE_NAME});

        onCreate(db);

    }

    // CRUD operations

    public void addItem(Item item){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.KEY_BABY_ITEM, item.getItemName());
        values.put(Constants.KEY_QUANTITY_NUMBER, item.getItemQuantity());
        values.put(Constants.KEY_COLOR, item.getItemColor());
        values.put(Constants.KEY_ITEM_SIZE,item.getItemSize());

        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        //Insert the row

        db.insert(TABLE_NAME, null, values);

        Log.d("Added", "addItem: Added Values");
    }

    //get an Item

    public Item getItem(int id) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{
                        Constants.KEY_ID, Constants.KEY_BABY_ITEM, Constants.KEY_QUANTITY_NUMBER,
                        Constants.KEY_COLOR, Constants.KEY_ITEM_SIZE, Constants.KEY_DATE_NAME},
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null,
                null, null, null
        );

        Item item = null;
        if (cursor != null) {

            cursor.moveToFirst();

            item = new Item();

            item.setId(Integer.parseInt
                    (cursor.getString(cursor.getColumnIndex(String.valueOf(Constants.KEY_ID)))));

            item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_BABY_ITEM)));
            item.setItemColor(cursor.getString(cursor.getColumnIndex(KEY_COLOR)));
            item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QUANTITY_NUMBER)));
            item.setItemSize(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ITEM_SIZE)));

            //Convert timestamp into readable time

            DateFormat dateFormat = DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(
                    new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

            item.setDateItemCreated(formattedDate);
        }


        return item;
    }

    //get all the items

    public List<Item> getAllItems(){

        SQLiteDatabase db = this.getReadableDatabase();

        List<Item> itemList = new ArrayList<>();

        String selectAll = "SELECT * FROM " + Constants.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()){

            do {

                Item item = new Item();

                item.setId(Integer.parseInt
                        (cursor.getString(cursor.getColumnIndex(String.valueOf(Constants.KEY_ID)))));

                item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_BABY_ITEM)));
                item.setItemColor(cursor.getString(cursor.getColumnIndex(KEY_COLOR)));
                item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QUANTITY_NUMBER)));
                item.setItemSize(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ITEM_SIZE)));

                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(
                        new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

                item.setDateItemCreated(formattedDate);

                //add to our arraylist

                itemList.add(item);


            }while (cursor.moveToNext());
        }

        return itemList;
    }

    //Todo: add updateItem

    public int updateItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.KEY_BABY_ITEM, item.getItemName());
        values.put(Constants.KEY_QUANTITY_NUMBER, item.getItemQuantity());
        values.put(Constants.KEY_COLOR, item.getItemColor());
        values.put(Constants.KEY_ITEM_SIZE,item.getItemSize());

        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        // update row

        return db.update(TABLE_NAME, values, Constants.KEY_ID + "=?",
                new String[]{String.valueOf(item.getId())});

    }

    //Todo: add add delete item

    public void deleteItem(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(TABLE_NAME, Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)});

        db.close();
    }
    //Todo: get Item count

    public int getItemsCount(){



    String countQuery = "SELECT * FROM " + TABLE_NAME;

    SQLiteDatabase db = this.getReadableDatabase();

    Cursor cursor = db.rawQuery(countQuery, null);

    return cursor.getCount();
    }

}
