package com.technoabinash.mig33;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.technoabinash.mig33.fragments.HomeFragment;
import com.technoabinash.mig33.fragments.NotificationFragment;
import com.technoabinash.mig33.fragments.ProfileFragment;
import com.technoabinash.mig33.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {
private BottomNavigationView bottomNavigationView;
private FrameLayout fragment;
private Fragment selectorFragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        bottomNavigationView=findViewById(R.id.bottom_nav);
        fragment=findViewById(R.id.fragment_layout);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        selectorFragments=new HomeFragment();
                        break;
                        case R.id.nav_search:
                        selectorFragments=new SearchFragment();
                        break;case R.id.nav_profile:
                        selectorFragments=new ProfileFragment();
                        break;
                    case R.id.nav_heart:
                        selectorFragments = new NotificationFragment();
                        break;
                        case R.id.nav_add:
                        selectorFragments = null;
                        startActivity(new Intent(getApplicationContext(),PostActivity.class));
                        break;
                }
                if (selectorFragments !=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,selectorFragments).commit();
                }
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,new HomeFragment()).commit();

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String profileId = intent.getString("publisherId");

            getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", profileId).apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new ProfileFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout , new HomeFragment()).commit();
        }
    }
}