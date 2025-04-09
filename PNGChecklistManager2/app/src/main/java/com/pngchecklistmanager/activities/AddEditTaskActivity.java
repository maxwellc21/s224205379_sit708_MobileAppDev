package com.pngchecklistmanager.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pngchecklistmanager.R;
import com.pngchecklistmanager.database.Task;
import com.pngchecklistmanager.database.TaskDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AddEditTaskActivity extends BaseActivity {

    EditText inputTitle, inputDesc, inputDate;
    AutoCompleteTextView categoryDropdown;
    Button saveBtn, deleteBtn;
    Task existingTask = null;
    boolean isFormatting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        inputTitle = findViewById(R.id.input_title);
        inputDesc = findViewById(R.id.input_description);
        inputDate = findViewById(R.id.input_date);
        categoryDropdown = findViewById(R.id.spinner_category);
        saveBtn = findViewById(R.id.btn_save);
        deleteBtn = findViewById(R.id.btn_delete);

        // Date formatting
        inputDate.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (isFormatting) return;
                isFormatting = true;
                String digits = s.toString().replaceAll("[^\\d]", "");
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < digits.length() && i < 8; i++) {
                    if (i == 4 || i == 6) formatted.append('-');
                    formatted.append(digits.charAt(i));
                }
                inputDate.setText(formatted.toString());
                inputDate.setSelection(formatted.length());
                isFormatting = false;
            }
        });

        inputDate.setOnClickListener(v -> showDatePicker());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.policy_categories, android.R.layout.simple_dropdown_item_1line
        );
        categoryDropdown.setAdapter(adapter);

        int taskId = getIntent().getIntExtra("task_id", -1);
        if (taskId != -1) {
            existingTask = TaskDatabase.getInstance(this).taskDao().getById(taskId);
            if (existingTask != null) {
                inputTitle.setText(existingTask.title);
                inputDesc.setText(existingTask.description);
                inputDate.setText(existingTask.dueDate);
                categoryDropdown.setText(existingTask.category, false);
                deleteBtn.setVisibility(Button.VISIBLE);
            }
        }

        saveBtn.setOnClickListener(v -> {
            String title = inputTitle.getText().toString().trim();
            String desc = inputDesc.getText().toString().trim();
            String date = inputDate.getText().toString().trim();
            String category = categoryDropdown.getText().toString().trim();

            if (title.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Title and Due Date are required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidDate(date)) {
                inputDate.setError("Date must be in format YYYY-MM-DD");
                return;
            }

            if (existingTask != null) {
                existingTask.title = title;
                existingTask.description = desc;
                existingTask.category = category;
                existingTask.dueDate = date;
                TaskDatabase.getInstance(this).taskDao().update(existingTask);
                Toast.makeText(this, "Task updated!", Toast.LENGTH_SHORT).show();
            } else {
                Task newTask = new Task(title, desc, category, date);
                TaskDatabase.getInstance(this).taskDao().insert(newTask);
                Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        deleteBtn.setOnClickListener(v -> {
            if (existingTask != null) {
                TaskDatabase.getInstance(this).taskDao().delete(existingTask);
                Toast.makeText(this, "Task deleted!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        setupBottomNav(bottomNav);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, day) -> {
                    String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day);
                    inputDate.setText(formattedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
