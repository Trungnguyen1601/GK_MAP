package com.example.project_test1.models;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<User> {

    FirebaseFirestore Database;
    public UserAdapter(Context context, ArrayList<User> Users) {
        super(context, 0, Users);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User User = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.student_item, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView idTextView = convertView.findViewById(R.id.idTextView);
        CheckBox xacthucCheckBox = convertView.findViewById(R.id.xacthucCheckBox);

        nameTextView.setText(User.getName());
        idTextView.setText(User.getID());
        xacthucCheckBox.setChecked(User.isXacthuc());

//        // Thêm OnCheckedChangeListener cho CheckBox
//        xacthucCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // Khi trạng thái của CheckBox thay đổi
//                // Cập nhật trạng thái của YourModel
//                User.setCheck(isChecked);
//
//                if (isChecked) {
//                    // Thực hiện tác vụ khi CheckBox được chọn
//                    // Ví dụ: Hiển thị thông báo, lưu vào cơ sở dữ liệu, v.v.
//                    Toast.makeText(getContext(), "CheckBox is checked", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Thực hiện tác vụ khi CheckBox được bỏ chọn
//                    // Ví dụ: Hiển thị thông báo, lưu vào cơ sở dữ liệu, v.v.
//                    Toast.makeText(getContext(), "CheckBox is unchecked", Toast.LENGTH_SHORT).show();
//                }
//
//
//                // Có thể thực hiện các hành động khác tùy thuộc vào nhu cầu của bạn
//                // Ví dụ: Lưu trạng thái vào cơ sở dữ liệu, cập nhật UI, v.v.
//            }
//        });

        xacthucCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khi CheckBox được nhấn
                // Thực hiện hành động với item tại vị trí position
                boolean isChecked = !User.isXacthuc();
                User.setCheck(isChecked);

                if (isChecked) {
                    // Thực hiện tác vụ khi CheckBox được chọn
                    // Ví dụ: Hiển thị thông báo, lưu vào cơ sở dữ liệu, v.v.
                    Toast.makeText(getContext(), "CheckBox is checked", Toast.LENGTH_SHORT).show();
                    Update_xacthuc(Database,User.getID(),isChecked);
                } else {
                    // Thực hiện tác vụ khi CheckBox được bỏ chọn
                    // Ví dụ: Hiển thị thông báo, lưu vào cơ sở dữ liệu, v.v.
                    Toast.makeText(getContext(), "CheckBox is unchecked", Toast.LENGTH_SHORT).show();
                    Update_xacthuc(Database,User.getID(),isChecked);
                }

                // Có thể thực hiện các hành động khác tùy thuộc vào nhu cầu của bạn
                // Ví dụ: Lưu trạng thái vào cơ sở dữ liệu, cập nhật UI, v.v.
            }
        });

        return convertView;
    }

    private void Update_xacthuc(FirebaseFirestore Database, String user_ID, boolean isChecked)
    {
        Database = FirebaseFirestore.getInstance();
        CollectionReference usersRef = Database.collection("users");
        //Query query = usersRef.whereEqualTo("name", targetName);
        Query query =usersRef.whereEqualTo("MSSV",user_ID);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // 3. Lấy reference của tài liệu cần cập nhật
                        DocumentReference docRef = usersRef.document(document.getId());

                        // 4. Sử dụng phương thức update() để cập nhật thông tin
                        docRef.update("diemdanh", isChecked)
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
