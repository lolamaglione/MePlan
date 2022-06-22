package com.lolamaglione.meplancapstone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.databinding.ActivityMainBinding;
import com.lolamaglione.meplancapstone.fragments.FeedFragment;
import com.parse.ParseUser;

/**
 * MainActivity handles the Main Fragments to make the app functionable, like the feedFragment
 * where you can see all the recipes and the list fragment, where you can see your grocery list
 * It also has a bottom naviagtion view to access these fragments
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ActivityMainBinding binding;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        bottomNavigationView = binding.bottomNavigation;

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.action_home:
                        fragment = new FeedFragment();
                        break;
                    case R.id.action_list:
                        break;
                    default:
                        fragment = new FeedFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
    }
}