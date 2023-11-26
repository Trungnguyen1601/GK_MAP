package com.example.project_test1.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.project_test1.Adapter.StudentAcceptPermissionAdapter;
import com.example.project_test1.Decoration.DividerItemDecoration;
import com.example.project_test1.R;
import com.example.project_test1.Untilities.Constants;
import com.example.project_test1.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AcceptPermissionActivity extends AppCompatActivity {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    List<User> studentList1 = new ArrayList<>();
    StudentAcceptPermissionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_permission);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int dividerColor = R.color.divider_color; // Màu sắc của đường line
        float dividerWidth = getResources().getDimensionPixelSize(R.dimen.divider_height); // Chiều rộng của đường line
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, dividerColor, dividerWidth);
        recyclerView.addItemDecoration(itemDecoration);

        List<User> studentList = generateStudentList();
        adapter = new StudentAcceptPermissionAdapter(studentList1);
        recyclerView.setAdapter(adapter);
    }

    private List<User> generateStudentList() {
        List<User> students = new ArrayList<>();
        User user = new User();
        CollectionReference usersRef = database.collection(Constants.KEY_COLLECTION_NAME);
        Query query = usersRef
                .whereEqualTo(Constants.KEY_PERMISSION,false)
                .whereEqualTo(Constants.KEY_ROLE,"student");

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String Name = document.getString(Constants.KEY_NAME);
                    String MSSV = document.getString(Constants.KEY_MSSV);
                    String Class = document.getString(Constants.KEY_CLASS);
                    String Image_encode = document.getString(Constants.KEY_IMAGE);
                    user.setName(Name);
                    user.setMSSV(MSSV);
                    user.setLop(Class);
                    user.setImage(Image_encode);
                    studentList1.add(user);
                    // Đây là tên lớp (className) của người dùng có tên (name) là "thanh"
                }
                adapter.notifyDataSetChanged();

            }
        });
        Toast.makeText(getApplicationContext(), ""+students.size(), Toast.LENGTH_SHORT).show();

        return students;
    }

}