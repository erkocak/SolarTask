package com.example.solartask;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solartask.db.CategoryDao;
import com.example.solartask.db.Database;
import com.example.solartask.db.Habit;
import com.example.solartask.db.HabitDao;
import com.example.solartask.db.Task;
import com.example.solartask.db.TaskDao;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import java.sql.SQLOutput;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the bottom bar
        BottomNavigationView  mBottomBar =  findViewById(R.id.bottomNavigationView);
        mBottomBar.setBackground(null);

        //setup navigation
        BottomNavigationView  bottomBarView =  findViewById(R.id.bottomNavigationView);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragNavHost);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomBarView, navController);

        //Getting the lists from database
        Database db = Room.databaseBuilder(getApplicationContext(),
                Database.class, "appDB").allowMainThreadQueries().build();

        TaskDao taskDao = db.taskDao();
        HabitDao habitDao = db.habitDao();

        // populate tasks
        if (taskDao.getAllTasks().size() == 0) {
            Date date = new Date();
            Task task = new Task("math hw", 1, false, date, "homework");
            Task task1 = new Task("eng hw", 1, false, date, "homework");
            Task task2 = new Task("run 1 km", 1, false, date, "sports");
            Task task3 = new Task("swim 1 km", 1, false, date, "sports");
            Task task4 = new Task("water the plants", 1, false, date, "chore");
            db.taskDao().insertAll(task, task1, task2, task3, task4);
        }

        if (habitDao.getAllHabits().size() == 0) {
            Habit habit = new Habit("drink 1 lt water", false);
            Habit habit1 = new Habit("not smoked all day", false);
            Habit habit2 = new Habit("eat healthy", false);
            Habit habit3 = new Habit("study 1 hour", false);
            db.habitDao().insertAll(habit, habit1, habit2, habit3);
        }

        List<Task> tasks = taskDao.getAllTasks();
        System.out.println(tasks);

        // settings navigation
        toolbar = findViewById(R.id.topAppBar);

        //Toolbar navigation
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.settings);
            }
        });

        FloatingActionButton addTaskButton = findViewById(R.id.fab);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    boolean isOnTasks = true;

    void showDialog() {
        //Inflating the layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.fragment_add_new_task, null);

        //Defining the buttons
        EditText editTextCategory = (EditText) view.findViewById(R.id.editTextCategory);
        EditText editTextNewTask = (EditText) view.findViewById(R.id.editTextNewTask);

        TextView importanceTitle = (TextView) view.findViewById(R.id.textView5);
        RadioGroup rgTaskType = (RadioGroup) view.findViewById(R.id.rgTaskType);

        Button taskChoiceButton = (Button) view.findViewById(R.id.taskChoiceButton);
        Button habitChoiceButton = (Button) view.findViewById(R.id.habitChoiceButton);

        Button dateSelection = (Button) view.findViewById(R.id.dateSelection);
        Button timeSelection = (Button) view.findViewById(R.id.timeSelection);

        //Setting up the buttons and arranging the visibilities for the AlertDialog
        rgTaskType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.rbOne:
                        rgTaskType.setVisibility(View.VISIBLE);
                        editTextCategory.setVisibility(View.VISIBLE);
                        editTextNewTask.setVisibility(View.VISIBLE);
                        dateSelection.setVisibility(View.VISIBLE);
                        timeSelection.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });

        taskChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOnTasks = true;
                rgTaskType.setVisibility(View.VISIBLE);
                editTextCategory.setVisibility(View.VISIBLE);
                editTextNewTask.setVisibility(View.VISIBLE);
                dateSelection.setVisibility(View.VISIBLE);
                timeSelection.setVisibility(View.VISIBLE);
            }
        });

        habitChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOnTasks = false;
                rgTaskType.setVisibility(View.GONE);
                importanceTitle.setVisibility(View.GONE);
                editTextCategory.setVisibility(View.VISIBLE);
                editTextNewTask.setVisibility(View.VISIBLE);
                editTextNewTask.setHint("New Habit");
                dateSelection.setVisibility(View.GONE);
                timeSelection.setVisibility(View.GONE);
            }
        });

        //Setting up the date and time pickers
        MaterialDatePicker datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .build();

        dateSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show( getSupportFragmentManager(), "datePicker");
            }
        });

        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build();

        timeSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.show(getSupportFragmentManager(), "timePicker");
            }
        });

        Button createNewTaskButton = view.findViewById(R.id.createNewTaskButton);

        //Initializing the AlertDialog
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        createNewTaskButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String newCategoryName = editTextCategory.getText().toString();
                String newTaskName = editTextNewTask.getText().toString();
                Date d = new Date();
                Habit h;
                Task t;

                Database db = Database.getInstance(getApplicationContext());

                if (isOnTasks) {
                    d = new Date(Long.parseLong(datePicker.getSelection().toString()) + timePicker.
                            getMinute() * 60 * 1000 + timePicker.getHour() * 60 * 60 * 1000);

                    //Setting up the importance
                    int importance = 0;
                    switch(rgTaskType.getCheckedRadioButtonId()) {
                        case R.id.rbOne:
                            importance = 4;
                            break;
                        case R.id.rbTwo:
                            importance = 3;
                            break;
                        case R.id.rbThree:
                            importance = 2;
                            break;
                        case R.id.rbFour:
                            importance = 1;
                            break;
                        default:
                            break;
                    }

                    t = new Task(newTaskName, importance, false, d, newCategoryName);

                    TaskDao taskDao = db.taskDao();
                    taskDao.insertAll(t);
                }
                else {
                    h = new Habit(newTaskName, false);

                    HabitDao habitDao = db.habitDao();
                    habitDao.insertAll(h);
                }

                //Closing the AlertDialog
                alertDialog.dismiss();

                //Setting up the navigation
                NavHostFragment navHostFragment =
                        (NavHostFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.fragNavHost);
                NavController navController = navHostFragment.getNavController();

                navController.navigate(R.id.tasks);
            }
        });

        alertDialog.show();
    }
}