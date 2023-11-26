package com.example.project_test1.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_test1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Mark_Activity extends AppCompatActivity {

    private TableLayout table;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);


        table = findViewById(R.id.student_table);
        db = FirebaseFirestore.getInstance();
        List<String[]> records = new ArrayList<>();
        String[] columnName = {"STT", "Name", "MSSV", "Grade"};

        db.collection("users")
                .whereEqualTo("role", "student")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String[] record = new String[4];
                            addHeaderRow(table, columnName);
                            int i = 1;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String tag = document.getReference().getPath();
                                record[0] = "" + i;
                                record[1] = document.getString("name");
                                record[2] = document.getString("MSSV");
                                record[3] = "" + document.getDouble("diem");
                                String title = record[1] + "\t\t" + record[2];
                                addDataRow(table, record, tag, title);
                                i++;
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void addHeaderRow(TableLayout tableLayout, String[] columnNames) {
        TableRow headerRow = new TableRow(this);
        float[] columnWeights = {1f, 2f, 1.5f, 1f};
        for (int i = 0; i < columnNames.length; i++) {

            TextView textView = this.createTextView(columnNames[i], true, false, "");
            textView.setTypeface(null, Typeface.BOLD);
            textView.setPadding(0, 25, 0, 25);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, columnWeights[i]);
            params.gravity = Gravity.CENTER;
            textView.setLayoutParams(params);
            headerRow.addView(textView);
        }

        tableLayout.addView(headerRow);
    }

    private void addDataRow(TableLayout tableLayout, String[] rowData, String ref, String title) {
        TableRow dataRow = new TableRow(this);
        dataRow.setTag(ref);
        for (int i = 0; i < rowData.length; i++) {
            TextView textView = this.createTextView(rowData[i], false, i == 3, title);
            float[] columnWeights = {1f, 2f, 1.5f, 1f};
            textView.setPadding(15, 20, 5, 20);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, columnWeights[i]);
            textView.setLayoutParams(params);
            textView.setText(rowData[i]);
            dataRow.addView(textView);
        }

        tableLayout.addView(dataRow);
    }

    @SuppressLint("ResourceAsColor")
    private TextView createTextView(String text, boolean isHeader, boolean isGrade, String title) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(10, 0, 10, 0);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        textView.setLayoutParams(params);
        textView.setBackgroundResource(R.drawable.cell_border);
        if (isHeader) {
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(R.color.header);
        }
        if (isGrade) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object tag = ((View) v.getParent()).getTag();
                    if (tag != null) {
                        String tagValue = tag.toString();
                        Log.d("ref", tagValue);
                        openEditDialog(text, tagValue, title);
                    }
                }
            });
        }

        return textView;
    }

    private void openEditDialog(final String currentValue, String ref, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        // Set up the input
        final EditText input = new EditText(this);
        input.setText(currentValue);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newValue = input.getText().toString();

                updateCell(newValue, ref);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateCell(String newValue, String ref) {
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("diem", Float.parseFloat(newValue));
        DocumentReference documentReference = db.document(ref);

        documentReference.update(updateData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Update success", Toast.LENGTH_SHORT).show();
                        ViewGroup vg = findViewById (R.id.student_table);
                        vg.invalidate();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Update fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}