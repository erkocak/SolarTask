package com.example.solartask;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.solartask.db.Database;
import com.example.solartask.db.Task;
import com.example.solartask.db.TaskDao;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Calendar extends Fragment {

    //Properties
    RecyclerView recyclerViewCalendar;
    RecyclerAdapter recyclerAdapterCalendar;
    ArrayList<Task> todaysTaskList;
    MaterialButton btnCalendarPriority;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    /**
     * Empty Calendar Constructor
     */
    public Calendar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Calendar.
     */
    public static Calendar newInstance(String param1, String param2) {
        Calendar fragment = new Calendar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        todaysTaskList = new ArrayList<>();

        //Getting the data from database and sets up today's task list
        Database db = Database.getInstance(getActivity().getApplicationContext());
        TaskDao taskDao = db.taskDao();
        for (Task t: taskDao.getAllTasks() ) {
            if( !t.done && DateUtils.isToday(t.getDueDate().getTime())) {
                todaysTaskList.add(t);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        //Sets up the RecyclerView and sets up RecyclerAdapter for Today's Agenda
        recyclerViewCalendar = v.findViewById(R.id.recyclerViewCalendar);

        recyclerAdapterCalendar = new RecyclerAdapter(todaysTaskList);
        recyclerViewCalendar.setAdapter(recyclerAdapterCalendar);

        //Sorting the list
        Collections.sort(todaysTaskList, new CustomComparatorDueDate());
        recyclerAdapterCalendar.notifyDataSetChanged();

        return v;
    }
}