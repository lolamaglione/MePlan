package com.lolamaglione.meplancapstone.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.lolamaglione.meplancapstone.ParseFacebookUtils;
import com.lolamaglione.meplancapstone.databinding.ActivityLoginBinding;
import com.lolamaglione.meplancapstone.fragments.MealPlanFragment;
import com.parse.ParseUser;
import java.util.Arrays;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import org.json.JSONException;
import java.util.Collection;

/**
 * Used to Login if a user is authorized through back4App
 * Can connect to SignupActivity if the user is not already a user.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignup;
    private ActivityLoginBinding binding;
    public static final String TAG = "LoginActivity";
    private Button btnFbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // if user is already logged in
        if(ParseUser.getCurrentUser() != null){
            goToMainActivity();
        }

        etUsername = binding.etUsername;
        etPassword = binding.etPassword;
        btnLogin = binding.btnLogin;
        btnSignup = binding.btnSignup;
        btnFbLogin = binding.btnFacebookLogin;

        // go to MainActivity if the user is created in Parse
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            loginUser(username, password);
        });

        // going to SignUp activity to create a new ParseUser
        btnSignup.setOnClickListener(v -> goToSignUp());

        btnFbLogin.setOnClickListener(v -> logInWithFB());
    }

    // login with facebook, implement oauth
    private void logInWithFB() {
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setTitle("Please, wait a moment.");
        dialog.setMessage("Logging in...");
        dialog.show();
        Collection<String> permissions = Arrays.asList("public_profile", "email");
        ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, permissions, (user, err) -> {
            dialog.dismiss();
            if (err != null) {
                Log.e("FacebookLoginExample", "done: ", err);
                Toast.makeText(LoginActivity.this, err.getMessage(), Toast.LENGTH_LONG).show();
            } else if (user == null) {
                Toast.makeText(LoginActivity.this, "The user cancelled the Facebook login.", Toast.LENGTH_LONG).show();
                Log.d("FacebookLoginExample", "Uh oh. The user cancelled the Facebook login.");
            } else if (user.isNew()) {
                Toast.makeText(LoginActivity.this, "User signed up and logged in through Facebook.", Toast.LENGTH_LONG).show();
                Log.d("FacebookLoginExample", "User signed up and logged in through Facebook!");
                getUserDetailFromFB();
            } else {
                Toast.makeText(LoginActivity.this, "User logged in through Facebook.", Toast.LENGTH_LONG).show();
                Log.d("FacebookLoginExample", "User logged in through Facebook!");
                showAlert("Hi," + user.getUsername(), "Welcome back!");
            }
        });
    }

    // use the LogInBackground method to login with username and password
    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null){
                Log.e(TAG, "Issue with Login " + e);
                Toast.makeText(LoginActivity.this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.i(TAG, "attempting to login: " + username);
            goToMainActivity();
        });
    }

    private void getUserDetailFromFB() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {
            ParseUser user = ParseUser.getCurrentUser();
            try {
                if (object.has("name"))
                    user.setUsername(object.getString("name"));
                if (object.has("email"))
                    user.setEmail(object.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            user.saveInBackground(e -> {
                if (e == null) {
                    showAlert("First Time Login!", "Welcome!");
                } else
                    showAlert("Error", e.getMessage());
            });
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("fragment", "meal plan");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    // create new Intent to MainActivity
    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fragment", "meal plan");
        this.startActivity(intent);
        finish();
    }

    // go to SignUp activity
    private void goToSignUp(){
        Intent intent = new Intent(this, SignUpActivity.class);
        this.startActivity(intent);
    }

}