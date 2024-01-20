package com.example.project_test1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_test1.R;

public class AfterForgotPasswordActivity extends AppCompatActivity {
    Button btnReturnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_forgotpassword_page);

        btnReturnLogin = findViewById(R.id.buttonReturnLogin);

        btnReturnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AfterForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
