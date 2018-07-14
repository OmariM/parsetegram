package me.omarim.parstegram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.File;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText handleInput;
    private EditText emailInput;
    private Button loginButton;
    private Button signupButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // find references to views
        usernameInput = findViewById(R.id.etUsername);
        passwordInput = findViewById(R.id.etPassword);
        handleInput = findViewById(R.id.etHandle);
        emailInput = findViewById(R.id.etEmail);
        loginButton = findViewById(R.id.btLogin);
        signupButton = findViewById(R.id.btSignup);
        registerButton = findViewById(R.id.btRegister);

        //hide the register stuff
        handleInput.setVisibility(View.GONE);
        emailInput.setVisibility(View.GONE);
        registerButton.setVisibility(View.GONE);

        //show the sign up stuff
        loginButton.setVisibility(View.VISIBLE);
        signupButton.setVisibility(View.VISIBLE);

        //persistence
        if (ParseUser.getCurrentUser() != null) {
            final Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();

                login(username, password);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: use the transition manager
                loginButton.setVisibility(View.GONE);
                signupButton.setVisibility(View.GONE);
                registerButton.setVisibility(View.VISIBLE);
                emailInput.setVisibility(View.VISIBLE);
                handleInput.setVisibility(View.VISIBLE);


            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                final String email = emailInput.getText().toString();
                final String handle = handleInput.getText().toString();

                createNewUser(username, handle, password, email);

            }
        });
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.d("LoginActivity", "login successful!");

                    // probably go to another intent
                    final Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Log.e("LoginActivity", "login failure :(");
                    e.printStackTrace();
                }
            }
        });
    }

    private void createNewUser(String username, String handle, String password, String email) {
        // create the new user
        ParseUser user = new ParseUser();
        // Set properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("handle", handle);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("LoginActivity", "login successful!");

                    // probably go to another intent
                    final Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Log.e("LoginActivity", "signup failure :(");
                    e.printStackTrace();
                }
            }
        });
    }


}
