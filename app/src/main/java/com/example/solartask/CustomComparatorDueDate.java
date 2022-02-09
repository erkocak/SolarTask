package com.example.solartask;

import com.example.solartask.db.Task;

import java.util.Comparator;

public class CustomComparatorDueDate implements Comparator<Task> {

    /**
     * Compares two tasks in terms of due dates
     * @param t1 task1
     * @param t2 task2
     * @return integer representation of comparison result
     */
    @Override
    public int compare(Task t1, Task t2) {
        return t1.getDueDate().compareTo( t2.getDueDate());
    }
}
