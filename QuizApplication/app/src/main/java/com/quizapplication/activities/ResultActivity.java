// activities/ResultActivity.java
package com.quizapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.quizapplication.R;

public class ResultActivity extends AppCompatActivity {

    private TextView txtFinalScore;
    private Button btnTakeNewQuiz, btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        txtFinalScore = findViewById(R.id.txtFinalScore);
        btnTakeNewQuiz = findViewById(R.id.btnTakeNewQuiz);
        btnFinish = findViewById(R.id.btnFinish);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);

        txtFinalScore.setText("Your Score: " + score + "/" + total);

        btnTakeNewQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clears backstack
            startActivity(intent);
            finish();
        });

        btnFinish.setOnClickListener(v -> finishAffinity()); // Exit app
    }
}
