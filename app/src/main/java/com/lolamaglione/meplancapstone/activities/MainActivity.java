package com.lolamaglione.meplancapstone.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lolamaglione.meplancapstone.R;
import com.lolamaglione.meplancapstone.databinding.ActivityMainBinding;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private TextView tvUsername;
    private ImageView ivProfile;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        tvUsername = binding.tvUsernameMain;
        ivProfile = binding.ivProfile;

        tvUsername.setText(ParseUser.getCurrentUser().getUsername());
    }
}