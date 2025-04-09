package com.pngchecklistmanager.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.pngchecklistmanager.R;
import com.pngchecklistmanager.database.Task;
import com.pngchecklistmanager.database.TaskDatabase;

public class TaskDetailActivity extends BaseActivity {
    TextView title, desc, category, dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        title = findViewById(R.id.detail_title);
        desc = findViewById(R.id.detail_description);
        category = findViewById(R.id.detail_category);
        dueDate = findViewById(R.id.detail_due_date);

        int taskId = getIntent().getIntExtra("task_id", -1);
        if (taskId != -1) {
            Task task = TaskDatabase.getInstance(this).taskDao().getById(taskId);
            if (task != null) {
                title.setText(task.title);
                desc.setText(task.description);
                category.setText(task.category);
                dueDate.setText(task.dueDate);
            }
        }

    }
}
