package com.example.solartask;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solartask.db.Database;
import com.example.solartask.db.Habit;
import com.example.solartask.db.HabitDao;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterHabits extends RecyclerView.Adapter<RecyclerAdapterHabits.ViewHolder> {

    //Properties
    private static final String TAG = "RecyclerAdapterHabits";

    ArrayList<String> habitsList;
    ArrayList<ViewHolder> views;
    List<Habit> allHabits;

    /**
     * Gets the initial properties from the database
     * @param habitsList the list to be initialized
     * @param d database
     */
    public RecyclerAdapterHabits(ArrayList<String> habitsList, Database d) {

        this.habitsList = habitsList;
        views = new ArrayList<ViewHolder>();
        HabitDao habitDao = d.habitDao();

        allHabits = habitDao.getAllHabits();
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
        View view = layoutInflater.inflate(R.layout.habit_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        views.add(viewHolder);

        return viewHolder;
    }

    /**
     * Gets the view that the RecyclerView holds
     * @param index position
     * @return the RecyclerView view
     */
    public ViewHolder getView(int index){
        return views.get(index);
    }

    /**
     * Holds separate properties on separate RecyclerView elements
     * @param holder RecyclerView element
     * @param position the element's line position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtHabit.setText(habitsList.get(position));

        if (allHabits.get(position).done ){
            holder.day1.setBackgroundResource(R.drawable.habit_success);
        }
    }

    /**
     * Gets the item count
     * @return habit list's size
     */
    @Override
    public int getItemCount() {
        return habitsList.size();
    }

    /**
     * ViewHolder inner class
     * @author Yağız Alkılıç
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtHabit;
        RelativeLayout day1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHabit = itemView.findViewById(R.id.txtHabit);
            //Success, Failure ids here
            day1 = itemView.findViewById(R.id.habitdoneday1);
        }

        public RelativeLayout getViewDay5()
        {
            return  day1;
        }
    }
}
