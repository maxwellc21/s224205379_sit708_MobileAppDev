package com.pngchecklistmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.pngchecklistmanager.R;
import com.pngchecklistmanager.adapters.TaskAdapter;
import com.pngchecklistmanager.database.Task;
import com.pngchecklistmanager.database.TaskDatabase;

import java.util.List;

public class MainActivity extends BaseActivity {

    RecyclerView recyclerView;
    TaskAdapter taskAdapter;
    Button btnSort;
    boolean sortByDate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        btnSort = findViewById(R.id.btn_sort);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadTasksSortedByDate();

        btnSort.setOnClickListener(v -> {
            sortByDate = !sortByDate;
            if (sortByDate) {
                loadTasksSortedByDate();
                btnSort.setText("Sort by Due Date");
            } else {
                loadTasksSortedByCategory();
                btnSort.setText("Sort by Category");
            }
        });

        fabAdd.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, AddEditTaskActivity.class);
            startActivity(intent);
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        setupBottomNav(bottomNav);
    }

    private void loadTasksSortedByDate() {
        List<Task> tasks = TaskDatabase.getInstance(this).taskDao().getAllByDueDate();
        taskAdapter = new TaskAdapter(this, tasks);
        recyclerView.setAdapter(taskAdapter);
    }

    private void loadTasksSortedByCategory() {
        List<Task> tasks = TaskDatabase.getInstance(this).taskDao().getAllByCategory();
        taskAdapter = new TaskAdapter(this, tasks);
        recyclerView.setAdapter(taskAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (sortByDate) {
            loadTasksSortedByDate();
        } else {
            loadTasksSortedByCategory();
        }
    }

}
