package com.pngchecklistmanager.activities;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pngchecklistmanager.R;

public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        setupBottomNav(bottomNav);
    }
}
