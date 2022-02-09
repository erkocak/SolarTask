package com.example.solartask;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Tasks extends Fragment {

    //Properties
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ArrayList<Task> tasksList;
    MaterialButton btnTaskPriority;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER (default android values)
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    /**
     * Empty Tasks Constructor
     */
    public Tasks() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tasks.
     */
    public static Tasks newInstance(String param1, String param2) {
        Tasks fragment = new Tasks();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting up the task list from database and initializing it from the database
        tasksList = new ArrayList<>();

        Database db = Database.getInstance(getActivity().getApplicationContext());
        TaskDao taskDao = db.taskDao();
        for (Task t: taskDao.getAllTasks() ) {

            if( ! t.done) {
                tasksList.add(t);
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tasks, container, false);

        //Setting up the RecyclerView for tasks.
        recyclerView = v.findViewById(R.id.recyclerView);

        recyclerAdapter = new RecyclerAdapter(tasksList);
        recyclerView.setAdapter(recyclerAdapter);

        Collections.sort(tasksList, new CustomComparatorDueDate());
        recyclerAdapter.notifyDataSetChanged();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration( recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //ONCLICK LISTENER FOR PRIORITY CHANGE!
        btnTaskPriority = (MaterialButton) v.findViewById(R.id.floatingActionButton);
        btnTaskPriority.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ( btnTaskPriority.getText().toString().equals("by Due Date")) {
                    Collections.sort(tasksList, new CustomComparator());
                    recyclerAdapter.notifyDataSetChanged();
                    btnTaskPriority.setText("by Importance");
                }
                else if ( btnTaskPriority.getText().toString().equals("by Importance")) {
                    Collections.sort(tasksList, new CustomComparatorDueDate());
                    recyclerAdapter.notifyDataSetChanged();
                    btnTaskPriority.setText("by Due Date");
                }
            }
        });

        return v;
    }

    //Setting up the swiping funciton for tasks
    Task deletedTask = null;
    ArrayList<Task> completedTasks = new ArrayList<>();

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();

            Database database = Database.getInstance(getActivity().getApplicationContext());
            TaskDao tDao = database.taskDao();

            switch (direction) {
                //Deleting swipe (left)
                case ItemTouchHelper.LEFT:
                    //TASK DELETION
                    deletedTask = tasksList.get(position);
                    tasksList.remove(position);
                    recyclerAdapter.notifyItemRemoved(position);

                    Snackbar.make(recyclerView, deletedTask.getDescription() + " deleted!", Snackbar.LENGTH_LONG)
                            .show();
                    tDao.deleteTasks(deletedTask);
                    break;

                //Completing swipe (right)
                case ItemTouchHelper.RIGHT:
                    //TASK COMPLETION
                    Task taskName = tasksList.get(position);
                    completedTasks.add(taskName);

                    tasksList.remove(position);
                    recyclerAdapter.notifyItemRemoved(position);

                    Snackbar.make(recyclerView, taskName.getDescription() + " completed!", Snackbar.LENGTH_LONG).show();

                    taskName.complete();
                    tDao.updateTask(taskName);

                    NavHostFragment navHostFragment =
                            (NavHostFragment) getActivity().getSupportFragmentManager()
                                    .findFragmentById(R.id.fragNavHost);
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(R.id.tasks);

                    break;
            }
        }

        //Setting up the swipe colors and icons
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(recyclerView.getContext(), R.color.colorPrimary))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(recyclerView.getContext(), R.color.colorCompletion))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_check_circle_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}