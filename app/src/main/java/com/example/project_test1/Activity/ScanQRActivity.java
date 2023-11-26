package com.example.project_test1.Activity;

import static android.content.ContentValues.TAG;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class ScanQRActivity extends AppCompatActivity {
    // Variables for getting current location
    private String locationIsNear = null;
    private String locationUser = null;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    // Variables for scanning QR code
    private CodeScanner mCodeScanner;
    boolean CameraPermission = false;
    final int CAMERA_PERM = 1;
    String email;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Function onCreate()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qractivity);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this,scannerView);

        // Check for location permissions
        if (checkLocationPermissions()) {
            // Permission already granted, start location updates
            startLocationUpdates();
        } else {
            // Permission not granted, request it
            requestLocationPermissions();
        }

        Intent intent = getIntent();
        if (intent != null) {
            email = intent.getStringExtra("email_QR");
            Log.d(TAG, "Email là "+ email);
            // Sử dụng dữ liệu ở đây
        }

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the user is near before starting the QR code scan
                Log.d(TAG, "Location is near: " + locationIsNear);

                if (locationIsNear != null && locationIsNear.equals("Yes")) {
                    mCodeScanner.startPreview();
                    mCodeScanner.setDecodeCallback(result -> {
                        runOnUiThread(() -> {
                            CollectionReference collectionRef = db.collection("users");
                            collectionRef.whereEqualTo("account", email)
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> handleFirestoreSuccess(queryDocumentSnapshots, result))
                                    .addOnFailureListener(e -> handleFirestoreFailure(e));
                        });
                    });
                } else {
                    Toast.makeText(ScanQRActivity.this, "User is not near the designated location", Toast.LENGTH_LONG).show();
                }
            }
        });

//        mCodeScanner.setDecodeCallback(result -> {
//            runOnUiThread(() -> {
//                CollectionReference collectionRef = db.collection("users");
//                collectionRef.whereEqualTo("account", email)
//                        .get()
//                        .addOnSuccessListener(queryDocumentSnapshots -> handleFirestoreSuccess(queryDocumentSnapshots, result))
//                        .addOnFailureListener(e -> handleFirestoreFailure(e));
//            });
//        });

    }

    // Function for decoded QR
    private void handleFirestoreSuccess(QuerySnapshot queryDocumentSnapshots, Result result) {
        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
            String date_dd = (String) documentSnapshot.get("Ngay_diemdanh");
            Toast.makeText(ScanQRActivity.this, result.getText(), Toast.LENGTH_LONG).show();

            if (result.getText().equals(date_dd)) {
                updateFirestoreAttendance(documentSnapshot);
            }
        }
    }

    private void updateFirestoreAttendance(DocumentSnapshot documentSnapshot) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = firestore.collection("users");

        collectionRef.whereEqualTo("account", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ArrayList<Map<String, Object>> ngayDiemDanh = (ArrayList<Map<String, Object>>) document.get("ngay");
                            updateAttendanceArray(document, ngayDiemDanh);
                        }
                    } else {
                        // Handle the case where task is not successful
                    }
                });
    }

    private void updateAttendanceArray(QueryDocumentSnapshot document, ArrayList<Map<String, Object>> ngayDiemDanh) {
        String date_dd = (String) document.get("Ngay_diemdanh");

        for (Map<String, Object> attendance : ngayDiemDanh) {
            String date = (String) attendance.get("date");

            if (date != null && date.equals(date_dd)) {
                attendance.put("presented", true);
                break;
            }
        }

        document.getReference().update("ngay", ngayDiemDanh)
                .addOnSuccessListener(aVoid -> {
                    // Update successful
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    private void handleFirestoreFailure(Exception e) {
        // Handle the failure to read from Firestore
    }

    // Function for getting current location
    private boolean checkLocationPermissions() {
        for (String permission : LOCATION_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, LOCATION_PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    private void startLocationUpdates() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                locationUser = String.valueOf(latitude)  + ", " + String.valueOf(longitude);

                Log.d(TAG, "User is at: " + locationUser);
                updateLocationInfo(latitude, longitude);

                checkLocation(latitude, longitude);
                Log.d(TAG, "is near?: " + locationIsNear);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
                askPermission();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
                askPermission();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Location providers are not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLocationInfo(double latitude, double longitude) {
        // Update your UI or perform any action with the new location information
        String locationInfo = "Latitude: " + latitude + "\nLongitude: " + longitude;
    }

    private boolean checkPermissionGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        // Location
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (checkPermissionGranted(grantResults)) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }

        // QR code
        if (requestCode == CAMERA_PERM){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                mCodeScanner.startPreview();
                CameraPermission = true;
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                    showCameraPermissionDialog();
                } else {
                    showSettingsPermissionDialog();
                }
            }
        }
    }

    public void checkLocation(double xPosition, double yPosition) {
        double allowedRadius = 0.00001;
        double distance = sqrt(pow(xPosition - 21.004010169142187 , 2) + pow(yPosition - 105.84266667946878 , 2));

        Log.d(TAG, "allowedRadius " + allowedRadius);
        Log.d(TAG, "distance " + distance);
        if (distance <= allowedRadius) {
            locationIsNear = "Yes";
            Toast.makeText(this, "User is near here", Toast.LENGTH_LONG).show();
        } else {
            locationIsNear = "No";
            Toast.makeText(this, "User is not near here", Toast.LENGTH_LONG).show();
        }
    }

    private void askPermission(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){
                ActivityCompat.requestPermissions(ScanQRActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERM);
            } else {
                mCodeScanner.startPreview();
                CameraPermission = true;
            }
        }
    }

    @Override
    protected void onPause() {
        if (CameraPermission){
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }

    private void showCameraPermissionDialog() {
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
    }

    private void showSettingsPermissionDialog() {
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
    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Quay lại Activity trước đó
        Intent intent = new Intent(this, MainActivity_Student.class);
        startActivity(intent);
    }

}