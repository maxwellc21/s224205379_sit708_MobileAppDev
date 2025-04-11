package com.quizapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.quizapplication.R;

public class CalculatorActivity extends AppCompatActivity {

    private TextInputEditText editTextNumber1, editTextNumber2;
    private TextView txtResult;
    private MaterialButton btnAdd, btnSubtract, btnClear;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        editTextNumber1 = findViewById(R.id.editTextNumber1);
        editTextNumber2 = findViewById(R.id.editTextNumber2);
        txtResult = findViewById(R.id.txtResult);
        btnAdd = findViewById(R.id.btnAdd);
        btnSubtract = findViewById(R.id.btnSubtract);
        btnClear = findViewById(R.id.btnClear);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        btnAdd.setOnClickListener(v -> calculate("add"));
        btnSubtract.setOnClickListener(v -> calculate("subtract"));
        btnClear.setOnClickListener(v -> {
            editTextNumber1.setText("");
            editTextNumber2.setText("");
            txtResult.setText("Result: ");
        });

        // Bottom navigation setup
        bottomNavigation.setSelectedItemId(R.id.nav_calculator);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_calculator) {
                return true;
            } else if (id == R.id.nav_quiz) {
                startActivity(new Intent(CalculatorActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    private void calculate(String operation) {
        try {
            double num1 = Double.parseDouble(editTextNumber1.getText().toString().trim());
            double num2 = Double.parseDouble(editTextNumber2.getText().toString().trim());
            double result = operation.equals("add") ? num1 + num2 : num1 - num2;
            txtResult.setText("Result: " + result);
        } catch (Exception e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }
}
