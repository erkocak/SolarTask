package com.example.solartask;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.solartask.db.Database;
import com.example.solartask.db.Task;
import com.example.solartask.db.TaskDao;

import java.util.ArrayList;
import java.util.Date;

public class OneTimeTaskReport extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Database db;
    TaskDao taskDao;
    ArrayList<Task> allTasks;

    private String mParam1;
    private String mParam2;

    /**
     * Empty OneTimeTaskReport Constructor
     */
    public OneTimeTaskReport() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Habits.
     */
    public static Habits newInstance(String param1, String param2) {
        Habits fragment = new Habits();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    /**
     * Get a list of tasks that were due in interval amount of time.
     * @param interval amount of time for the report to go back to
     * @return tasks that were due for last interval amount of days
     */
    private ArrayList<Task> getOneTimeTasksInInterval(long interval)
    {
        ArrayList<Task> lastWeekTasks = new ArrayList<Task>();

        long DAY_IN_MS = 1000 * 60 * 60 * 24;

        Database db = Database.getInstance(getActivity().getApplicationContext());
        TaskDao taskDao = db.taskDao();
        ArrayList<Task> allTasks = (ArrayList<Task>) taskDao.getAllTasks();

        for( int index = 0; index < allTasks.size(); index++ )
        {
            Task currentTask = (Task) allTasks.get(index);
            Date timeAfterInterval = new Date(((new Date().getTime()) - (interval * DAY_IN_MS)));
            if( currentTask.dueDate.after(timeAfterInterval) )
            {
                lastWeekTasks.add(currentTask);
            }
        }
        return lastWeekTasks;
    }

    /**
     * Give the percentage of one-time tasks completed in given time.
     * @param interval amount of time for the report to go back to
     * @return averageTaskReport percentage of one-time tasks completed in an interval.
     */
    public double avarageOneTimeTaskReport(long interval)
    {
        ArrayList<Task> oneTimeTasks = getOneTimeTasksInInterval(interval);
        double amountOfCompletedTasks = 0;
        double totalTasks = 0;

        // iterate through the tasks and get how many were done
        for( int index = 0; index < oneTimeTasks.size(); index++ )
        {
            if( oneTimeTasks.get(index).done)
            {
                amountOfCompletedTasks++;
            }
            totalTasks++;
        }
        return amountOfCompletedTasks / totalTasks * 100;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_one_time_task_report, container, false);

        // Set month and week bigger smaller
        int month_percentage = (int) avarageOneTimeTaskReport(30);
        int week_percentage = (int) avarageOneTimeTaskReport(7);

        TextView monthText = (TextView) v.findViewById(R.id.month_percentage);
        monthText.setText("%"+ month_percentage);

        TextView weekText = (TextView) v.findViewById(R.id.week_percentage);
        weekText.setText("%" + week_percentage);

        RelativeLayout weekCircle = (RelativeLayout) v.findViewById(R.id.circleweek);

        if (week_percentage < month_percentage){
            weekCircle.setBackgroundResource(R.drawable.oval_failure);
        }
        else if (week_percentage > month_percentage){
            weekCircle.setBackgroundResource(R.drawable.oval_success);
        }
        else {
            weekCircle.setBackgroundResource(R.drawable.oval);
        }

        //Setting up the navigation
        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fragNavHost);
        NavController navController = navHostFragment.getNavController();

        final Button button = (Button) v.findViewById(R.id.buttononetimefunctional);
        final Button buttonUseless = (Button) v.findViewById(R.id.buttononetimeuseless);

        //Setting up the button navigation
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                navController.navigate(R.id.action_one_time_task_to_reports);
                System.out.println("testing");
            }
        });

        buttonUseless.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                navController.navigate(R.id.action_one_time_task_to_reports);
            }
        });

        return v;
    }
}