package com.example.solartask;

import com.example.solartask.db.Task;

import java.util.Comparator;

public class CustomComparator implements Comparator<Task> {

    /**
     * Compares two tasks in terms of importance
     * @param t1 task1
     * @param t2 task2
     * @return integer representation of comparison result
     */
    @Override
    public int compare(Task t1, Task t2) {
        return t2.getImportance().compareTo( t1.getImportance());
    }
}
