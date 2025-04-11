package com.quizapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.quizapplication.R;
import com.quizapplication.models.Question;
import com.quizapplication.network.ApiClient;
import com.quizapplication.network.QuizApiService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {

    private TextView txtQuestion, txtHint, txtExplanation, loadingText;
    private RadioGroup radioGroupOptions;
    private RadioButton[] radioButtons;
    private Button btnSubmit;
    private ProgressBar progressBar;
    private LinearLayout loadingOverlay;

    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        txtQuestion = findViewById(R.id.txtQuestion);
        txtHint = findViewById(R.id.txtHint);
        txtExplanation = findViewById(R.id.txtExplanation);
        radioGroupOptions = findViewById(R.id.radioGroupOptions);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);
        loadingOverlay = findViewById(R.id.loadingOverlay);
        loadingText = findViewById(R.id.loadingText);

        radioButtons = new RadioButton[]{
                findViewById(R.id.option1),
                findViewById(R.id.option2),
                findViewById(R.id.option3),
                findViewById(R.id.option4)
        };

        String topic = getIntent().getStringExtra("topic");
        String difficulty = getIntent().getStringExtra("difficulty");

        fetchQuestions(topic, difficulty);
    }

    private void fetchQuestions(String topic, String difficulty) {
        loadingOverlay.setVisibility(View.VISIBLE);
        btnSubmit.setEnabled(false);

        QuizApiService apiService = ApiClient.getClient().create(QuizApiService.class);
        Map<String, String> body = new HashMap<>();
        body.put("topic", topic);
        body.put("difficulty", difficulty);

        apiService.getQuiz(body).enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                loadingOverlay.setVisibility(View.GONE);
                btnSubmit.setEnabled(true);

                if (response.isSuccessful()) {
                    questions = response.body();
                    if (questions == null || questions.isEmpty()) {
                        Toast.makeText(QuizActivity.this, "No quiz questions found", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                    progressBar.setMax(questions.size());
                    loadQuestion();
                } else {
                    Toast.makeText(QuizActivity.this, "Error loading quiz", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                loadingOverlay.setVisibility(View.GONE);
                btnSubmit.setEnabled(true);
                t.printStackTrace();
                Toast.makeText(QuizActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        new Handler().postDelayed(() -> {
            if (loadingOverlay.getVisibility() == View.VISIBLE) {
                loadingText.setText("Still working...\nLLaMA AI may take up to 1 minute to generate your quiz.");
            }
        }, 15000);
    }

    private void loadQuestion() {
        if (currentIndex >= questions.size()) {
            Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("total", questions.size());
            startActivity(intent);
            finish();
            return;
        }

        Question q = questions.get(currentIndex);
        txtQuestion.setText(q.question);
        txtHint.setText("");
        txtExplanation.setText("");
        radioGroupOptions.clearCheck();

        for (int i = 0; i < 4; i++) {
            if (i < q.options.size()) {
                radioButtons[i].setVisibility(View.VISIBLE);
                radioButtons[i].setText(q.options.get(i));
                radioButtons[i].setTextColor(getResources().getColor(android.R.color.black));
            } else {
                radioButtons[i].setVisibility(View.GONE);
            }
        }

        progressBar.setProgress(currentIndex + 1);
        answered = false;

        btnSubmit.setText("Submit");
        btnSubmit.setOnClickListener(view -> {
            if (answered) {
                currentIndex++;
                loadQuestion();
                return;
            }

            int selectedId = radioGroupOptions.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedIndex = radioGroupOptions.indexOfChild(findViewById(selectedId));
            int correctIndex = q.correctAnswers.get(0);

            if (selectedIndex == correctIndex) {
                radioButtons[selectedIndex].setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                score++;
            } else {
                radioButtons[selectedIndex].setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                if (correctIndex < radioButtons.length) {
                    radioButtons[correctIndex].setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                }
            }

            txtHint.setText("Hint: " + q.hint);
            txtExplanation.setText("Explanation: " + q.explanation);
            btnSubmit.setText("Next");
            answered = true;
        });
    }
}
