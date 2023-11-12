package com.example.project_test1.Activity;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.project_test1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;

import org.jetbrains.annotations.NotNull;

public class ScanQRActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    boolean CameraPermission = false;
    final int CAMERA_PERM = 1;
    String email;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qractivity);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this,scannerView);
        askPermission();


        Intent intent = getIntent();
        if (intent != null) {
            email = intent.getStringExtra("email_QR");
            Log.d(TAG, "Email là "+ email);
            // Sử dụng dữ liệu ở đây
        }
        if (CameraPermission) {

            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mCodeScanner.startPreview();

                }
            });

            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull @NotNull Result result) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(ScanQRActivity.this, result.getText(), Toast.LENGTH_LONG).show();
                            if (result.getText().equals("trung")) {

                                String targetName = "trung"; // Tên người dùng cần cập nhật
                                db = FirebaseFirestore.getInstance();
                                CollectionReference usersRef = db.collection("users");
                                //Query query = usersRef.whereEqualTo("name", targetName);
                                Query query = usersRef.whereEqualTo("account", email);


                                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                // 3. Lấy reference của tài liệu cần cập nhật
                                                DocumentReference docRef = usersRef.document(document.getId());

                                                // 4. Sử dụng phương thức update() để cập nhật thông tin
                                                docRef.update("diemdanh", true)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d(TAG, "Document updated successfully");

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error updating document", e);
                                                            }
                                                        });
                                            }
                                        } else {
                                            Log.w(TAG, "Error getting documents.", task.getException());
                                        }
                                    }
                                });


                            }
                        }
                    });

                }
            });

        }
    }

    private void askPermission(){

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){

                ActivityCompat.requestPermissions(ScanQRActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERM);


            }else {

                mCodeScanner.startPreview();
                CameraPermission = true;
            }

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        if (requestCode == CAMERA_PERM){


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                mCodeScanner.startPreview();
                CameraPermission = true;
            }else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){

                    new AlertDialog.Builder(this)
                            .setTitle("Permission")
                            .setMessage("Please provide the camera permission for using all the features of the app")
                            .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    ActivityCompat.requestPermissions(ScanQRActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERM);

                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                }
                            }).create().show();

                }else {

                    new AlertDialog.Builder(this)
                            .setTitle("Permission")
                            .setMessage("You have denied some permission. Allow all permission at [Settings] > [Permissions]")
                            .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package",getPackageName(),null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();


                                }
                            }).setNegativeButton("No, Exit app", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                    finish();

                                }
                            }).create().show();



                }

            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        if (CameraPermission){
            mCodeScanner.releaseResources();
        }

        super.onPause();
    }
}