package com.example.project_test1.Helper;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.Map;

public class FireStore_Help {

    FirebaseFirestore firestore;

    static public void Update_All_Documment_String(FirebaseFirestore firestore, String collection, String Field, String Value)
    {

        CollectionReference collectionRef = firestore.collection(collection);

        collectionRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Thêm trường mới cho mỗi tài liệu
                        document.getReference().update(Field, Value);
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                });
    }

    static public void Update_All_Documment_Boolean(FirebaseFirestore firestore, String collection, String Field, Boolean Value)
    {

        CollectionReference collectionRef = firestore.collection(collection);

        collectionRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Thêm trường mới cho mỗi tài liệu
                        document.getReference().update(Field, Value);
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                });
    }

    static public void Update_All_Documment_Map(FirebaseFirestore firestore,Map<String, Object> entry, String collection, String field )
    {
        FirebaseFirestore finalDb1 = firestore;
        firestore.collection(collection).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            WriteBatch batch = finalDb1.batch();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference docRef = finalDb1.collection(collection).document(document.getId());
                                batch.update(docRef, field, FieldValue.arrayUnion(entry));
                            }

                            batch.commit()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Cập nhật cho tất cả document thành công
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Xử lý khi cập nhật thất bại
                                        }
                                    });
                        } else {
                            // Xử lý khi không thể lấy được danh sách documents
                        }
                    }
                });
    }

}
