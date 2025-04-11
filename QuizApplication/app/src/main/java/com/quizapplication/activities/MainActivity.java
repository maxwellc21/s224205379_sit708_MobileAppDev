package com.quizapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.quizapplication.R;
import com.quizapplication.utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText editTextName;
    private MaterialAutoCompleteTextView spinnerTopic, spinnerDifficulty;
    private MaterialButton btnStartQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        spinnerTopic = findViewById(R.id.spinnerTopic);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);

        // Restore name from preferences if available
        String savedName = SharedPrefManager.getName(this);
        if (!savedName.isEmpty()) {
            editTextName.setText(savedName);
        }

        // Set up topic dropdown
        String[] topics = {"Java Programming", "Python", "Android", "Cyber Security"};
        ArrayAdapter<String> topicAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, topics);
        spinnerTopic.setAdapter(topicAdapter);

        // Set up difficulty dropdown
        String[] levels = {"Easy", "Medium", "Hard"};
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, levels);
        spinnerDifficulty.setAdapter(difficultyAdapter);

        // Optional: Show dropdown on click (forces it to behave like Spinner)
        spinnerTopic.setOnClickListener(v -> spinnerTopic.showDropDown());
        spinnerDifficulty.setOnClickListener(v -> spinnerDifficulty.showDropDown());

        // Start Quiz button click
        btnStartQuiz.setOnClickListener(view -> {
            String name = editTextName.getText().toString().trim();
            String topic = spinnerTopic.getText().toString().trim();
            String difficulty = spinnerDifficulty.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (topic.isEmpty() || difficulty.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please select topic and difficulty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save name for future
            SharedPrefManager.saveName(MainActivity.this, name);

            // Go to QuizActivity
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("topic", topic);
            intent.putExtra("difficulty", difficulty);
            startActivity(intent);
        });
        findViewById(R.id.btnOpenCalculator).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CalculatorActivity.class));
        });

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_quiz); // current page

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_quiz) {
                return true; // already on Quiz
            } else if (id == R.id.nav_calculator) {
                startActivity(new Intent(MainActivity.this, CalculatorActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;

        });


    }
}
