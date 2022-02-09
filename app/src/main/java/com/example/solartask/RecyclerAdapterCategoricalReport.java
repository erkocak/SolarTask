package com.example.solartask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerAdapterCategoricalReport extends RecyclerView.Adapter<RecyclerAdapterCategoricalReport.ViewHolder> {

    //Properties
    private static final String TAG = "RecyclerAdapterCategoricalReport";

    ArrayList<String> doneAmountList;
    ArrayList<ViewHolder> views;

    /**
     * Gets the initial properties
     * @param doneAmountList the list to be initialized
     */
    public RecyclerAdapterCategoricalReport(ArrayList<String> doneAmountList) {

        this.doneAmountList = doneAmountList;
        views = new ArrayList<ViewHolder>();
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
        View view = layoutInflater.inflate(R.layout.category_report_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        views.add(viewHolder);

        return viewHolder;
    }

    /**
     * Changes separate properties from separate RecyclerView elements
     * @param holder RecyclerView element
     * @param position the element's line position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtCategory.setText(doneAmountList.get(position));
    }

    /**
     * Gets the item count
     * @return done task list's size
     */
    @Override
    public int getItemCount() {
        return doneAmountList.size();
    }

    /**
     * ViewHolder inner class
     * @author Yağız Alkılıç
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCategory;
        TextView txtAmountDone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategory);
        }
    }
}
