package com.example.babyneeds;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText babyItem, itemQuantity, itemColor, itemSize;

    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHandler = new DatabaseHandler(this);

        byPassActivity();

        //check if item was saved

        List<Item> items = databaseHandler.getAllItems();
        
        for (Item item:items){

            Log.d("kioko", "onCreate: " +item.getDateItemCreated());
        }



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopupDialog();

                Snackbar.make(view, "Add items in the popup dialog", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void byPassActivity() {

        if (databaseHandler.getItemsCount() > 1){

            startActivity(new Intent(MainActivity.this, SecondActivity.class));
            finish();
        }


    }

    private void saveItem(View view) {
        //Todo: save each baby item to db
        Item item = new Item();

        String newItem = babyItem.getText().toString().trim();
        String newColor = itemColor.getText().toString().trim();
        int quantity = Integer.parseInt(itemQuantity.getText().toString().trim());
        int size = Integer.parseInt(itemSize.getText().toString().trim());

        item.setItemName(newItem);
        item.setItemColor(newColor);
        item.setItemQuantity(quantity);
        item.setItemSize(size);


        databaseHandler.addItem(item);

        Snackbar.make(view,"Item saved", Snackbar.LENGTH_SHORT).show();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {

                dialog.dismiss();

                startActivity(new Intent(MainActivity.this, SecondActivity.class));


//            }long delayMillis:1200f;
//        });

        //Todo: move to next screen for details
    }


    private void createPopupDialog(){

        builder = new  AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.popup, null);

        babyItem = view.findViewById(R.id.babyItem);
        itemQuantity = view.findViewById(R.id.quantity);
        itemColor = view.findViewById(R.id.color);
        itemSize = view.findViewById(R.id.size);

        saveButton = view.findViewById(R.id.btnSave);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!babyItem.getText().toString().isEmpty()
                        && !itemColor.getText().toString().isEmpty()
                        && !itemQuantity.getText().toString().isEmpty()
                        && !itemSize.getText().toString().isEmpty()){

                    saveItem(v);
                }else{

                    Snackbar.make(v, "Empty fields are not allowed", Snackbar.LENGTH_SHORT)
                            .show();
                }

            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
