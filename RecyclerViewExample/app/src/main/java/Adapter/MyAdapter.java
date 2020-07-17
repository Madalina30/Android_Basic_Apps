package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mady.recyclerviewexample.DetailsActivity;
import com.mady.recyclerviewexample.R;

import java.util.List;

import Model.ListItem;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    //declarations
    private Context context;
    private List<ListItem> listItem;

    //constructor
    public MyAdapter(Context context, List listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create a view that gets its "parent" view and inflates the layout with the item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        //returning the new object constructor
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        //to see the items from the data source - combine resources
        ListItem item = listItem.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.rating.setText(item.getRating());

    }

    @Override
    public int getItemCount() { //count on the view size
        return listItem.size();
    }
    //the item view - on click
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //declarations
        public TextView name;
        public TextView description;
        public  TextView rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            //instantiate
            name = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            rating = itemView.findViewById(R.id.rating);

        }

        @Override
        public void onClick(View v) {
            //get position of the row clicked or tapped
            int position = getAdapterPosition();
            ListItem item = listItem.get(position);
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("name", item.getName());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("rating", item.getRating());

            context.startActivity(intent);

//            Toast.makeText(context, item.getName(), Toast.LENGTH_SHORT).show();

        }
    }
}
