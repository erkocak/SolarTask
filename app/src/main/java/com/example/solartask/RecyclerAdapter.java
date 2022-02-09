package com.example.solartask;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solartask.db.Task;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    //Properties
    private static final String TAG = "RecyclerAdapter";

    ArrayList<Task> tasksList;
    public RecyclerAdapter(ArrayList<Task> tasksList) {
        this.tasksList = tasksList;
    }

    /**
     * When the page is displayed, creates the list of separate views at corresponding items in RecyclerView
     * @param parent
     * @param viewType
     * @return viewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflates the layout
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.tasks_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    /**
     * Holds separate properties on separate RecyclerView elements
     * @param holder RecyclerView element
     * @param position the element's line position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTask.setText(tasksList.get(position).getDescription());
        holder.txtCategory.setText(tasksList.get(position).getCategory());
        holder.txtDueDate.setText(tasksList.get(position).getDueDate().toString());
    }

    /**
     * Gets the item count
     * @return done task list's size
     */
    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    /**
     * ViewHolder inner class
     * @author Burak Erkoçak
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTask, txtCategory, txtDueDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTask = itemView.findViewById(R.id.txtTask);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtDueDate = itemView.findViewById(R.id.txtDueDate);
        }
    }

}
