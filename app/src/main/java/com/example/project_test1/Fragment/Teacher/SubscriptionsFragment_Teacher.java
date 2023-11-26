package com.example.project_test1.Fragment.Teacher;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.project_test1.Helper.FireStore_Help;
import com.example.project_test1.Adapter.LessonAdapter;
import com.example.project_test1.R;
import com.example.project_test1.models.Lesson;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscriptionsFragment_Teacher#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionsFragment_Teacher extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listView;
    LessonAdapter adapter;

    private FirebaseFirestore firestore;
    int[] count_student = new int[16];
    int[] count_student_dd = new int[16];



    public SubscriptionsFragment_Teacher() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubscriptionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubscriptionsFragment_Teacher newInstance(String param1, String param2) {
        SubscriptionsFragment_Teacher fragment = new SubscriptionsFragment_Teacher();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscriptions_teacher, container, false);
        listView = view.findViewById(R.id.listView);
        List<Lesson> lessonList = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();
        adapter = new LessonAdapter(getContext(), new ArrayList<>());
        listView.setAdapter(adapter);
        listView.setClickable(true);

        CollectionReference collectionReference = firestore.collection("users");
        FirebaseFirestore finalDb = firestore;

        collectionReference.whereEqualTo("role","student")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    ArrayList<Lesson> lessonList = new ArrayList<>();

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int week_stt = 1;
                            int week_int = 1;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ArrayList<Map<String, Object>> LessonList = (ArrayList<Map<String, Object>>) document.get("ngay");
                                if (LessonList != null) {
                                    for (Map<String, Object> data : LessonList) {
                                        if (data.get("week") instanceof Long) {
                                            Long week = (Long) data.get("week"); // Ép kiểu Object thành Long
                                            week_int = week.intValue(); // Chuyển đổi thành giá trị int
                                            // Sử dụng giá trị intValue ở đây
                                        } else {
                                            // Xử lý khi objValue không phải là một đối tượng Long
                                        }

                                        Boolean isPresented = (Boolean) data.get("presented");
                                        if ((week_int == week_stt) )
                                        {
                                            count_student[week_stt] ++;
                                            if (Boolean.TRUE.equals(isPresented))
                                            {
                                                count_student_dd[week_stt] ++;
                                            }
                                        }
                                        week_stt++;
                                    }


                                }
                                week_stt = 1;
                            }
                            for(int i = 1; i <= 15; i++ )
                            {
                                Lesson lesson = new Lesson(i,count_student[i],count_student_dd[i]);
                                Map<String,Object> data = new HashMap<>();

                                data.put("week",lesson.getWeek());
                                data.put("Siso",lesson.getSL_SV());
                                data.put("SLSVdiemdanh",lesson.getSL_SV_Diemdanh());

                                //FireStore_Help.Edit_All_Documment_Map_In_Lesson(firestore,data,"Lesson","lesson");
                                FireStore_Help.editAllDocumentsInLesson(firestore,data,"Lesson","lesson");
                                lessonList.add(lesson);
                            }

                            adapter.clear();
                            adapter.addAll(lessonList);
                            adapter.notifyDataSetChanged();


                        } else {
                            // Xử lý khi không thể lấy được danh sách documents
                        }
                    }
                });


//        Lesson lesson = new Lesson(1,86,86,86);
//        Lesson lesson1 = new Lesson(2,86,86,86);
//        Lesson lesson2 = new Lesson(3,86,86,86);
//
//        lessonList.add(lesson);
//        lessonList.add(lesson1);
//        lessonList.add(lesson2);



        // Inflate the layout for this fragment
        return view;
    }
}