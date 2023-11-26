package com.example.project_test1.Fragment.Student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.project_test1.Adapter.AttendanceAdapter;
import com.example.project_test1.R;
import com.example.project_test1.databinding.FragmentHomeStudentBinding;
import com.example.project_test1.models.Attendance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Map;
import java.util.List;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment_Student#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment_Student extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FragmentHomeStudentBinding binding;
    private ListView listView;

    FirebaseUser user;

    private AttendanceAdapter adapter;
    private FirebaseFirestore db;

    Button refreshBtn;


    public HomeFragment_Student() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment_Student.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment_Student newInstance(String param1, String param2) {
        HomeFragment_Student fragment = new HomeFragment_Student();
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
        View view = inflater.inflate(R.layout.fragment_home_student, container, false);
        refreshBtn = view.findViewById(R.id.refreshButton);
        binding = FragmentHomeStudentBinding.inflate(inflater, container, false);
        adapter = new AttendanceAdapter(getActivity(), new ArrayList<>());

        user = FirebaseAuth.getInstance().getCurrentUser();

        binding.listDate.setAdapter(adapter);
        binding.listDate.setClickable(true);

        db = FirebaseFirestore.getInstance();

        loadAttendance();

        binding.refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show();
                loadAttendance();
            }
        });

        return binding.getRoot();

    }

    private void loadAttendance() {
        db.collection("users")
                .whereEqualTo("account",user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            ArrayList<Attendance> attendances = new ArrayList<>();
                            for(QueryDocumentSnapshot document : task.getResult()) {

                                if (document.exists()) {
                                    List<Map<String, Object>> attendArray = (List<Map<String, Object>>) document.get("ngay");

                                    if (attendArray != null) {
                                        for (Map<String, Object> attend : attendArray) {
                                            String date = (String) attend.get("date");
                                            Log.d("date", date);
                                            int week = ((Long) attend.get("week")).intValue();
                                            Boolean isAttendance = (Boolean) attend.get("presented");

                                            Attendance a = new Attendance(week, date, isAttendance);
                                            attendances.add(a);
                                        }
                                    }
                                }
                            }

                            adapter.clear();
                            adapter.addAll(attendances);
                            adapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                });
    }
    // Phương thức để cập nhật UI khi có sự thay đổi
    private void updateUI() {
        // Cập nhật UI của bạn ở đây
        // Ví dụ: Hiển thị thông báo, cập nhật ListView, v.v.
        adapter.notifyDataSetChanged();
    }
}