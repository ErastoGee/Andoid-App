package com.example.babyneeds;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.Item;
import com.example.babyneeds.ui.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "Chek it";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private FloatingActionButton fab;
    private List<Item> itemList;
    private DatabaseHandler databaseHandler;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private Button saveButton;
    private EditText babyItem, itemQuantity, itemColor, itemSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);

        databaseHandler = new DatabaseHandler(this);
         recyclerView.setHasFixedSize(true);


         recyclerView.setLayoutManager(new LinearLayoutManager(this));

         itemList = new ArrayList<>();
          //Get items from db

        itemList = databaseHandler.getAllItems();

        for (Item item:itemList){

            Log.d(TAG, "onCreate: " +item.getItemName());
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopDialog();
            }
        });
    }

    private void createPopDialog() {

        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null   );

        babyItem = view.findViewById(R.id.babyItem);
        itemQuantity = view.findViewById(R.id.quantity);
        itemColor = view.findViewById(R.id.color);
        itemSize = view.findViewById(R.id.size);

        saveButton = view.findViewById(R.id.btnSave);

        builder.setView(view);

        alertDialog= builder.create();
        alertDialog.show();

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
        alertDialog.dismiss();

        startActivity(new Intent(SecondActivity.this, SecondActivity.class));
        finish();

//            }long delayMillis:1200f;
//        });




}
}
