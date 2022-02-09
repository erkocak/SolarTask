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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.solartask.db.CategoryDao;
import com.example.solartask.db.Database;
import com.example.solartask.db.Habit;
import com.example.solartask.db.HabitDao;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class Habits extends Fragment {

    //Properties
    RecyclerView recyclerViewHabits;
    RecyclerAdapterHabits recyclerAdapterHabits;
    ArrayList<String> habitElements;
    Database db;
    HabitDao habitDao;
    List<Habit> allHabits;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    Spinner dropdownMenu;
    int touchCounter = 0;

    /**
     * Empty Habits Constructor
     */
    public Habits() {
        // Required empty public constructor
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
        habitElements = new ArrayList<>();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //Initializing the database
        db = Database.getInstance(getActivity().getApplicationContext());
        habitDao = db.habitDao();
        allHabits = habitDao.getAllHabits();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Setting up the habit list
        for (int index = 0; index < allHabits.size(); index++ ){
            habitElements.add(allHabits.get(index).name);
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_habits, container, false);

        recyclerViewHabits = v.findViewById(R.id.recyclerViewHabits);

        recyclerAdapterHabits = new RecyclerAdapterHabits(habitElements,db);
        recyclerViewHabits.setAdapter(recyclerAdapterHabits);

        dropdownMenu = v.findViewById(R.id.spinner1);
        initspinnerfooter();

        return v;
    }

    /**
     * Creates the spinner element and initializes various habit fragments
     */
    private void initspinnerfooter() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, habitElements);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownMenu.setAdapter(adapter);
        dropdownMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                String habitToSetAsDone = ((TextView) parent.getChildAt(0)).getText().toString();
                if (habitElements.indexOf(habitToSetAsDone) == 0 && touchCounter < 1 ){
                    touchCounter++;
                }
                else {
                    int recyclerIndex = habitElements.indexOf(habitToSetAsDone);
                    allHabits.get(recyclerIndex).complete();
                    habitDao.updateHabit(allHabits.get(recyclerIndex));
                    recyclerAdapterHabits.getView(recyclerIndex).getViewDay5().setBackgroundResource(R.drawable.habit_success);
                    touchCounter++;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

}