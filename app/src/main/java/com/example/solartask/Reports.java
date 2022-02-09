package com.example.solartask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.solartask.db.Database;
import com.example.solartask.db.Task;
import com.example.solartask.db.TaskDao;
import java.util.ArrayList;

public class Reports extends Fragment {

    //Properties
    private Button button1;
    Database db;
    TaskDao taskDao;
    ArrayList<Task> allTasks;
    ArrayList<String> categoricalList;

    /**
     * Empty Reports Constructor
     */
    public Reports() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Reports.
     */
    public static Reports newInstance() {
        Reports fragment = new Reports();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Connecting the Reports database
        db = Database.getInstance(getActivity().getApplicationContext());
        taskDao = db.taskDao();
        allTasks = (ArrayList<Task>) taskDao.getAllTasks();

        categoricalList = categoricalReport();

        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_reports, container, false);

        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.fragNavHost);
        NavController navController = navHostFragment.getNavController();

        RecyclerView recyclerCategoricalReports = V.findViewById(R.id.recyclerViewCategoricalReports);

        RecyclerAdapterCategoricalReport recyclerAdapterCategoricalReports = new RecyclerAdapterCategoricalReport(categoricalList);
        recyclerCategoricalReports.setAdapter(recyclerAdapterCategoricalReports);

        //Setting up the buttons and navigation
        final Button button = (Button) V.findViewById(R.id.buttoncategoryfunctional);
        final Button buttonUseless = (Button) V.findViewById(R.id.buttoncategoryuseless);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                navController.navigate(R.id.action_reports_to_one_time_task);
                System.out.println("testing");
            }
        });

        buttonUseless.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                navController.navigate(R.id.action_reports_to_one_time_task);
            }
        });

        return V;
    }

    /**
     * Get the amount of tasks or habits done in a category type
     * @param category type to look at
     * @return total amount of tasks or habits
     */
    public int getAmountDoneInCategory(String category)
    {
        int amountDone = 0;

        // look at tasks depending on task type add to the amount
        db = Database.getInstance(getActivity().getApplicationContext());
        taskDao = db.taskDao();
        allTasks = (ArrayList<Task>) taskDao.getAllTasks();

        System.out.println(allTasks);
        for( int index = 0; index < allTasks.size(); index++ )
        {
            if ( allTasks.get(index).category.equals(category) )
            {
                Task currentTask = (Task) allTasks.get(index);
                if ( currentTask.done )
                {
                    amountDone++;
                }
            }
        }
        return amountDone;
    }

    /**
     * Look at every category and give amount of tasks or habits done in them
     * @return categories with amount done in them
     */
    public ArrayList<String> categoricalReport() {
        ArrayList<String> categoryList = new ArrayList<String>();
        ArrayList<String> doneAmountList = new ArrayList<String>();

        db = Database.getInstance(getActivity().getApplicationContext());
        taskDao = db.taskDao();
        allTasks = (ArrayList<Task>) taskDao.getAllTasks();

        for (int index = 0; index < allTasks.size(); index++) {
            if (!categoryList.contains(allTasks.get(index).category)) {
                categoryList.add(allTasks.get(index).category);
            }
        }
        for (int index = 0; index < categoryList.size(); index++) {
            doneAmountList.add(categoryList.get(index) + ":" + getAmountDoneInCategory(categoryList.get(index)));
        }

        return doneAmountList;
    }
}