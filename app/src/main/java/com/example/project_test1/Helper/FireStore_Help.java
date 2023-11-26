package com.example.project_test1.Helper;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Map;

public class FireStore_Help {


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

    static public void Edit_All_Documment_Map_In_Lesson(FirebaseFirestore firestore,Map<String, Object> map, String collection1, String field )
    {
        CollectionReference usersRef = firestore.collection(collection1);
        Query query = usersRef.whereEqualTo("ID","1");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // 3. Lấy reference của tài liệu cần cập nhật
                        ArrayList<Map<String, Object>> TuanDiemDanh = (ArrayList<Map<String, Object>>) document.get("lesson");
                        int week_int = 0;
                        int data_int = 0;
                        int Sl_int = 0;
                        int Sl_dd_int = 0;
                        data_int = (int) map.get("week");
                        Sl_int = (int) map.get("Siso");
                        Sl_dd_int = (int)map.get("SLSVdiemdanh");
                        Log.d("Check data",""+data_int);
                        Log.d("Check Si so",""+map.get("Siso"));
                        Log.d("Check diem danh",""+map.get("SLSVdiemdanh"));
                        // Loop through the array to find the specific date and update its "present" value
                        for (Map<String, Object> tuan : TuanDiemDanh) {
                            if (tuan.get("week") instanceof Long) {
                                Long week = (Long) tuan.get("week"); // Ép kiểu Object thành Long
                                week_int = week.intValue(); // Chuyển đổi thành giá trị int
                                // Sử dụng giá trị intValue ở đây
                            } else {
                                // Xử lý khi objValue không phải là một đối tượng Long
                            }



                            Log.d("Check week",""+week_int);
                            if (week_int == data_int)
                            {
                                tuan.put("week", week_int);
                                tuan.put("Siso",12);
                                tuan.put("SLSVdiemdanh",12);
                            }

                        }

                        // Update the modified array back to Firestore
                        document.getReference().update("lesson1", TuanDiemDanh)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Thành công", "Thành công");
                                        // Update successful
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failure
                                    }
                                });
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }
    public static void editAllDocumentsInLesson(FirebaseFirestore firestore, Map<String, Object> map, String collection1, String field) {
        CollectionReference lessonsRef = firestore.collection(collection1);
        Query query = lessonsRef.whereEqualTo("ID", "1");

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    ArrayList<Map<String, Object>> lessonList = (ArrayList<Map<String, Object>>) document.get(field);

                    int week_int = (int) map.get("week");
                    int Sl_int = (int) map.get("Siso");
                    int Sl_dd_int = (int) map.get("SLSVdiemdanh");

                    for (Map<String, Object> Week : lessonList) {
                        int week = ((Long) Week.get("week")).intValue();

                        if (week == week_int) {
                            Week.put("Siso", Sl_int);
                            Week.put("SLSVdiemdanh", Sl_dd_int);

                        }
                    }

                    document.getReference().update(field, lessonList)
                            .addOnSuccessListener(aVoid -> Log.d("Thành công", "Cập nhật thành công"))
                            .addOnFailureListener(e -> Log.e("Lỗi", "Lỗi khi cập nhật", e));
                }
            } else {
                Log.w("Lỗi", "Lỗi khi lấy dữ liệu", task.getException());
            }
        });
    }

}
