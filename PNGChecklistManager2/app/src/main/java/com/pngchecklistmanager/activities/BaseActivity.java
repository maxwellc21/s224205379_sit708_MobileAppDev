package com.pngchecklistmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import com.pngchecklistmanager.R;

public class BaseActivity extends AppCompatActivity {

    protected void setupBottomNav(BottomNavigationView bottomNav) {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_tasks) {
                if (!(this instanceof MainActivity)) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            } else if (id == R.id.nav_about) {
                if (!(this instanceof AboutActivity)) {
                    startActivity(new Intent(this, AboutActivity.class));
                    finish();
                }
            }
            return true;
        });

        if (this instanceof MainActivity) {
            bottomNav.setSelectedItemId(R.id.nav_tasks);
        } else if (this instanceof AboutActivity) {
            bottomNav.setSelectedItemId(R.id.nav_about);
        }
    }
}
