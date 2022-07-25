package com.lolamaglione.meplancapstone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.databinding.ActivityMainBinding;
import com.lolamaglione.meplancapstone.fragments.FeedFragment;
import com.lolamaglione.meplancapstone.fragments.GroceryListFragment;
import com.lolamaglione.meplancapstone.fragments.MealPlanFragment;
import com.parse.ParseUser;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.facebook.login.LoginManager;
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
    public static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setSelectedItemId(R.id.action_plan);
        Fragment fragment = new MealPlanFragment();
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.action_home:
                        fragment = new FeedFragment();
                        break;
                    case R.id.action_list:
                        fragment = new GroceryListFragment();
                        break;
                    default:
                        fragment = new MealPlanFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout){
            logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Please, wait a moment.");
        dialog.setMessage("Logging out...");
        dialog.show();
        LoginManager.getInstance().logOut();
        ParseUser.logOut();
        Log.i(TAG, "attempting ot logout user");
        ParseUser currentUser = ParseUser.getCurrentUser();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}