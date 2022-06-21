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

public class MainActivity extends AppCompatActivity {

    private TextView tvUsername;
    private ImageView ivProfile;
    private BottomNavigationView bottomNavigationView;
    private ActivityMainBinding binding;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        tvUsername = binding.tvUsernameMain;
//        ivProfile = binding.ivProfile;
        bottomNavigationView = binding.bottomNavigation;


        //tvUsername.setText(ParseUser.getCurrentUser().getUsername());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.action_home:
                        fragment = new FeedFragment();
                        Toast.makeText(MainActivity.this, "home!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_list:
                        Toast.makeText(MainActivity.this, "List!", Toast.LENGTH_SHORT).show();
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