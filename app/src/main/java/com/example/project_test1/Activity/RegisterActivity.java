package com.example.project_test1.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_test1.Untilities.Constants;
import com.example.project_test1.Untilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.example.project_test1.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    public EditText signupEmail, signupPassword,signupconfirmPassword, signupMSSV, signupClass,signupName ;
    Button btnSignUp;
    TextView loginRedirectText;
    private String encodeImage;
    FrameLayout layoutImage;
    ProgressBar progressBar;

    RoundedImageView imageProfile;
    TextView textAddImage;
    FirebaseAuth firebaseAuth;

    CheckBox checkPassword;

    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        preferenceManager = new PreferenceManager(getApplicationContext());
        //
        firebaseAuth = FirebaseAuth.getInstance();
        signupName = findViewById(R.id.inputName);
        signupEmail = findViewById(R.id.inputEmail);
        signupPassword = findViewById(R.id.inputPassword);
        btnSignUp = findViewById(R.id.buttonSignUp);
        loginRedirectText = findViewById(R.id.textSignIn);
        signupconfirmPassword = findViewById(R.id.inputConfirmPassword);
        signupMSSV = findViewById(R.id.inputMSSV);
        signupClass = findViewById(R.id.inputClass);
        layoutImage = findViewById(R.id.layoutImage);
        imageProfile = findViewById(R.id.imageProfile);
        textAddImage = findViewById(R.id.textAddImage);
        progressBar = findViewById(R.id.progressBar);
        checkPassword = findViewById(R.id.showPassword);

        //
        setListeners();
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(RegisterActivity.this, com.example.project_test1.Activity.LoginActivity.class);
                startActivity(I);
            }
        });

    }

    private void Loading(Boolean isLoading)
    {
        if(isLoading)
        {
            btnSignUp.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            btnSignUp.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    private String encodeImage(Bitmap bitmap)
    {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT );
    }
    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignUpDetail()
    {
        if(encodeImage == null)
        {
            showToast("Select Profile image");
            return false;
        }
        else if (signupName.getText().toString().trim().isEmpty())
        {
            signupName.setError("Provide your Name first!");
            signupName.requestFocus();
            return false;
        }
        else if (signupMSSV.getText().toString().trim().isEmpty())
        {
            signupMSSV.setError("Provide your MSSV first");
            signupMSSV.requestFocus();
            return false;
        }
        else if(signupClass.getText().toString().trim().isEmpty())
        {
            signupClass.setError("Provide your Class");
            signupClass.requestFocus();
            return false;
        }
        else if (signupEmail.getText().toString().trim().isEmpty())
        {
            signupEmail.setError("Provide your Email");
            signupEmail.requestFocus();
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(signupEmail.getText().toString()).matches())
        {
            showToast("Enter valid email");
            return false;
        }
        else if (signupPassword.getText().toString().trim().isEmpty())
        {
            signupPassword.setError("Provide your Password");
            signupPassword.requestFocus();
            return false;
        }
        else if (signupconfirmPassword.getText().toString().trim().isEmpty())
        {
            signupconfirmPassword.setError("Provide your Password");
            signupconfirmPassword.requestFocus();
            return false;
        }
        else if (!signupPassword.getText().toString().equals(signupconfirmPassword.getText().toString()))
        {
            showToast("Password and confirm password must be same");
            return false;
        }
        else
        {
            return true;
        }
    }

    private void setListeners()
    {
        loginRedirectText.setOnClickListener(v -> onBackPressed());
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidSignUpDetail())
                {
                    SignUp();
                }
            }
        });
        layoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });

        checkPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    signupPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    signupconfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    signupPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    signupconfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK)
                {
                    Uri imageUri = result.getData().getData();

                    InputStream inputStream = null;
                    try {
                        inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageProfile.setImageBitmap(bitmap);
                        textAddImage.setVisibility(View.GONE);
                        encodeImage = encodeImage(bitmap);

                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
    );

    private void SignUp()
    {
        Loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME,signupName.getText().toString());
        user.put(Constants.KEY_EMAIL,signupEmail.getText().toString());
        user.put(Constants.KEY_CLASS,signupClass.getText().toString());
        user.put(Constants.KEY_MSSV,signupMSSV.getText().toString());
        user.put(Constants.KEY_ROLE,"student");
        user.put(Constants.KEY_PERMISSION,false);
        user.put(Constants.KEY_IMAGE,encodeImage);

        firebaseAuth.createUserWithEmailAndPassword(signupEmail.getText().toString(),signupPassword.getText().toString()).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if (!task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this.getApplicationContext(),
                            "SignUp unsuccessful: " + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    //startActivity(new Intent(RegisterActivity.this, MainActivity_Teacher.class));
                }
            }
        });
        database.collection(Constants.KEY_COLLECTION_NAME)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                    preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME,signupName.getText().toString());
                    preferenceManager.putString(Constants.KEY_MSSV,signupMSSV.getText().toString());
                    preferenceManager.putString(Constants.KEY_CLASS,signupClass.getText().toString());
                    preferenceManager.putString(Constants.KEY_ROLE,"student");

                    preferenceManager.putString(Constants.KEY_IMAGE,encodeImage);
                    Intent intent = new Intent(getApplicationContext(), MainActivity_Student.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    Loading(false);
                    showToast(exception.getMessage());

                });
    }

}
