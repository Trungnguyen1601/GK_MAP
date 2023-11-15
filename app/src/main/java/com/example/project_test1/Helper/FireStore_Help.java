package com.example.project_test1.Helper;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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

}
