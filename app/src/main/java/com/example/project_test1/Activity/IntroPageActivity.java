package com.example.project_test1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_test1.R;

public class IntroPageActivity extends AppCompatActivity {
    TextView textViewLogin;
    Button buttonSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_page);

        textViewLogin = findViewById(R.id.btnAlreadyAccount);
        buttonSignUp = findViewById(R.id.btnSignUpWithEmail);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroPageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroPageActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
