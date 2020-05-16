package com.example.babyneeds.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babyneeds.R;
import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Item> itemList){

        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.list_row, viewGroup,false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {

        Item item = itemList.get(position);

        viewHolder.itemName.setText(item.getItemName());
        viewHolder.itemColor.setText("Color: "+item.getItemColor());
        viewHolder.quantity.setText("Quantity: " +item.getItemQuantity());
        viewHolder.size.setText("Size: " +item.getItemSize());
        viewHolder.dateAdded.setText("Date added: "+item.getDateItemCreated());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        public int id;

        public TextView itemName, itemColor, quantity, size, dateAdded;
        public Button editButton, deleteButton;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;

            itemName = itemView.findViewById(R.id.itemName);
            itemColor = itemView.findViewById(R.id.itemColor);
            quantity = itemView.findViewById(R.id.itemQuantity);
            size = itemView.findViewById(R.id.itemSize);
            dateAdded = itemView.findViewById(R.id.itemDate);

            editButton = itemView.findViewById(R.id.btnEdit);
            deleteButton = itemView.findViewById(R.id.btnDelete);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position;
            position = getAdapterPosition();
            Item item = itemList.get(position);

            switch (v.getId()) {

                case R.id.btnEdit:

                    editItem(item);

                    break;

                case R.id.btnDelete:

                    deleteItem(item.getId());
                    break;
            }

        }

        private void deleteItem(final int id) {

            builder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);

            View view = inflater.inflate(R.layout.confimationpop, null);

            final DatabaseHandler db = new DatabaseHandler(context);

            Button yesButton = view.findViewById(R.id.btnYes);
            Button noButton = view.findViewById(R.id.btnNo);

            builder.setView(view);
            alertDialog = builder.create();
            alertDialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.deleteItem(id);
                    itemList.remove(getAdapterPosition());

                    notifyItemRemoved(getAdapterPosition());

                    alertDialog.dismiss();

                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertDialog.dismiss();

                }
            });


        }



        private void editItem(final Item newItem) {

//            Item item = itemList.get(getAdapterPosition());

            builder = new AlertDialog.Builder(context);

            inflater =LayoutInflater.from(context);

            final View view = inflater.inflate(R.layout.popup, null);

            Button saveButton;
            final EditText babyItem, itemQuantity, itemColor, itemSize;
            TextView title;

            babyItem = view.findViewById(R.id.babyItem);
            itemQuantity = view.findViewById(R.id.quantity);
            itemColor = view.findViewById(R.id.color);
            itemSize = view.findViewById(R.id.size);
            title = view.findViewById(R.id.title);

            saveButton = view.findViewById(R.id.btnSave);

            saveButton.setText("Update");

            title.setText("Edit Item");

            babyItem.setText(newItem.getItemName());
            itemColor.setText(newItem.getItemColor());
            itemQuantity.setText("" +newItem.getItemQuantity());
            itemSize.setText("" +newItem.getItemSize());

            builder.setView(view);

            alertDialog= builder.create();
            alertDialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHandler databaseHandler = new DatabaseHandler(context);

                    newItem.setItemName(babyItem.getText().toString());
                    newItem.setItemColor(itemColor.getText().toString());
                    newItem.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString()));
                    newItem.setItemQuantity(Integer.parseInt(itemSize.getText().toString()));

                    if ( !babyItem.getText().toString().isEmpty()
                    && !itemColor.getText().toString().isEmpty()
                    && !itemQuantity.getText().toString().isEmpty()
                    && !itemSize.getText().toString().isEmpty()){

                        databaseHandler.updateItem(newItem);
                        notifyItemChanged(getAdapterPosition(), newItem);

                    }else{
                        Snackbar.make(view, "Empty fields not allowed", Snackbar.LENGTH_SHORT).show();


                    }

                    alertDialog.dismiss();


                }
            });


        }
    }

}