package com.example.project_test1.Activity;

import static android.content.ContentValues.TAG;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import android.Manifest;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;

import org.jetbrains.annotations.NotNull;

public class ScanQRActivity extends AppCompatActivity {
    // Variables for getting current locaion
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
        askPermission();

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
                            if (result.getText().equals("Ngay 10/10/2023")) {

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

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Handle location updates
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                locationUser = String.valueOf(latitude)  + ", " + String.valueOf(longitude);

//                locationUser = latitude + "" + ", " + longitude + "";
                Log.d(TAG, "User is at: " + locationUser);
                // Use latitude and longitude as needed
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

        // Check for network provider availability
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Use the network provider for location updates
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // If the network provider is not available, use the GPS provider
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            // Handle the case when neither network nor GPS providers are available
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

    public void checkLocation(double xPosition, double yPosition) {
        double radius = 10.0;
        double distance = sqrt(pow(xPosition - 21.004010169142187 ,2) + pow(yPosition - 105.84266667946878 ,2) );

        Log.d(TAG, "radius " + radius);
        Log.d(TAG, "distance " + distance);
        if (distance <= radius) {
            locationIsNear = "Yes";
            Toast.makeText(this, "User is near hear", Toast.LENGTH_LONG).show();
//            mDatabaseReference.child("locationIsNear").setValue("Yes");
        }
        else {
            locationIsNear = "No";
            Toast.makeText(this, "User is not near hear", Toast.LENGTH_LONG).show();
//            mDatabaseReference.child("locationIsNear").setValue("No");
        }
    }

    // Functions for scanning QR code
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
        // Location
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (checkPermissionGranted(grantResults)) {
                // Permission granted, start location updates
                startLocationUpdates();
            } else {
                // Permission denied, show a message or handle it accordingly
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }

        // QR code
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