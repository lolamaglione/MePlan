package com.lolamaglione.meplancapstone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lolamaglione.meplancapstone.databinding.ActivitySignUpBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etEmail;
    private Button btnSignUp;
    private ActivitySignUpBinding binding;
    public static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(R.layout.activity_sign_up);

        etUsername = binding.etUsernameSign;
        etPassword = binding.etPasswordSign;
        etEmail = binding.etEmailSign;
        btnSignUp = binding.btnSignUpSign;

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();
                signUpUser(username, password, email);
            }
        });
    }

    private void signUpUser(String username, String password, String email) {
        ParseUser newUser = new ParseUser();
        newUser.put("username", username);
        newUser.put("password", password);
        newUser.put("email", email);
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "issue with Signup " + e);
                }
            }
        });
    }
}