package com.mady.grocerylist.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.google.android.material.snackbar.Snackbar;
import com.mady.grocerylist.Activities.MainActivity;
import com.mady.grocerylist.Data.DatabaseHandler;
import com.mady.grocerylist.Model.Grocery;
import com.mady.grocerylist.R;

import java.util.List;

public class RecyclerViewHandler extends RecyclerView.Adapter<RecyclerViewHandler.ViewHandler> {

    private Context context;
    private List<Grocery> groceryItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewHandler(Context context, List<Grocery> groceryItems) {
        this.context = context;
        this.groceryItems = groceryItems;
    }

    @NonNull
    @Override
    public RecyclerViewHandler.ViewHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        return new ViewHandler(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHandler.ViewHandler holder, int position) {
        Grocery grocery = groceryItems.get(position);

        holder.groceryItemName.setText(grocery.getName());
        holder.quantity.setText(grocery.getQuantity());
        holder.dateAdded.setText(grocery.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public class ViewHandler extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView groceryItemName;
        public TextView quantity;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHandler(@NonNull View view, Context ctx) {
            super(view);
            context = ctx;

            groceryItemName = view.findViewById(R.id.name);
            quantity = view.findViewById(R.id.quantity);
            dateAdded = view.findViewById(R.id.date);
            editButton = view.findViewById(R.id.edit);
            deleteButton = view.findViewById(R.id.delete);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //go to next screen - will delete later
                }
            });
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.edit:
                    int position1 = getAdapterPosition();
                    Grocery grocery1 = groceryItems.get(position1);
                    editItem(grocery1);

                    break;

                case R.id.delete:
                    int position = getAdapterPosition();
                    Grocery grocery = groceryItems.get(position);
                    deleteItem(grocery.getId());
                    break;
            }
        }

        @SuppressLint("SetTextI18n")
        public void editItem(final Grocery grocery) {
            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.popup, null);

            final EditText groceryItem = view.findViewById(R.id.enterItem);
            final EditText groceryQtt = view.findViewById(R.id.enterQtt);
            final TextView text = view.findViewById(R.id.tile);
            Button saveButton = view.findViewById(R.id.saveButton);

            text.setText("Edit Grocery Item");
            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHandler db = new DatabaseHandler(context);

                    //update item
                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(groceryQtt.getText().toString());

                    if (!groceryItem.getText().toString().isEmpty()
                            && !groceryQtt.getText().toString().isEmpty()) {
                        db.updateGrocery(grocery);
                        notifyItemChanged(getAdapterPosition(), grocery);
                    } else {
                        Snackbar.make(view, "Add grocery and quantity", Snackbar.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });

        }

        public void deleteItem(final int id) {
            //create alert
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton = view.findViewById(R.id.noButton);
            Button yesButton = view.findViewById(R.id.yesButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete the item
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteGrocery(id);
                    groceryItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    if (db.getGroceriesCount() == 0) {
                        context.startActivity(new Intent(context, MainActivity.class));
                        Activity activity = (Activity) context;
                        activity.finish();
                    }
                    dialog.dismiss();
                }
            });

        }
    }
}
