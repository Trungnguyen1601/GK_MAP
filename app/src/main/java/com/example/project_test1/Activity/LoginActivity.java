package com.example.project_test1.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.project_test1.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    public EditText loginEmail, loginPassword;
    Button btnLogin;
    TextView registerRedirectText, forgotPasswordText;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        firebaseAuth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.editEmail);
        loginPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        registerRedirectText = findViewById(R.id.signUpWithEmail);
        forgotPasswordText = findViewById(R.id.forgotPassword);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    loginEmail.setText(user.getEmail());
                    CollectionReference usersRef = db.collection("users");
                    Query query = usersRef.whereEqualTo("account", user.getEmail());

                    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                String className = (String) document.getString("role");
                                Boolean permission = (Boolean) document.getBoolean("permission");
                                if (className.equals("student") && permission)
                                {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity_Student.class);
                                    intent.putExtra("email", user.getEmail());
                                    startActivity(intent);

                                }
                                if (className.equals("teacher"))
                                {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity_Teacher.class);
                                    intent.putExtra("email", user.getEmail());
                                    startActivity(intent);

                                }
                                if (!permission)
                                {
                                   showToast("Permission denied");
                                }
                                // Đây là tên lớp (className) của người dùng có tên (name) là "thanh"
                            }
                        }
                    });


                } else {
                    Toast.makeText(LoginActivity.this, "Login to continue", Toast.LENGTH_SHORT).show();
                }
            }
        };

        registerRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(I);
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(I);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khai báo Firebase
                CollectionReference usersRef = db.collection("users");
                String userEmail = loginEmail.getText().toString();
                String userPassword = loginPassword.getText().toString();

                if (userEmail.isEmpty()) {
                    loginEmail.setError("Provide your Email first!");
                    loginEmail.requestFocus();
                } else if (userPassword.isEmpty()) {
                    loginPassword.setError("Enter Password!");
                    loginPassword.requestFocus();
                } else if (userEmail.isEmpty() && userPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(userEmail.isEmpty() && userPassword.isEmpty())) {
                    firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Not sucessfull", Toast.LENGTH_SHORT).show();
                            } else {
                                Query query = usersRef.whereEqualTo("account", userEmail);

                                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            String className = (String) document.get("role");
                                            Boolean permission = (Boolean) document.get("permission");
                                            if (className.equals("student") && permission)
                                            {
                                                Intent intent = new Intent(LoginActivity.this, MainActivity_Student.class);
                                                intent.putExtra("email", userEmail);
                                                startActivity(intent);

                                            }
                                            if (className.equals("teacher"))
                                            {
                                                Intent intent = new Intent(LoginActivity.this, MainActivity_Teacher.class);
                                                intent.putExtra("email", userEmail);
                                                startActivity(intent);

                                            }
                                            if(!permission)
                                            {
                                                showToast("Permission dined");
                                            }
                                            // Đây là tên lớp (className) của người dùng có tên (name) là "thanh"
                                        }
                                    }
                                });


                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);

    }
}