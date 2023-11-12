package com.example.project_test1.Fragment.Teacher;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.project_test1.Activity.DetailedActivity;
import com.example.project_test1.R;
import com.example.project_test1.models.User;
import com.example.project_test1.models.UserAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShortsFragment_Teacher#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShortsFragment_Teacher extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ListView listView;
    private ImageView btn_Refresh;
    private ImageButton btn_QRCode;
    private UserAdapter adapter;
    private FirebaseFirestore firestore;
    public ShortsFragment_Teacher() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShortsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShortsFragment_Teacher newInstance(String param1, String param2) {
        ShortsFragment_Teacher fragment = new ShortsFragment_Teacher();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shorts_teacher, container, false);
        listView = view.findViewById(R.id.listView);
        btn_Refresh = view.findViewById(R.id.btn_refresh);
        btn_QRCode = view.findViewById(R.id.btn_MakeQRCode);
        adapter = new UserAdapter(getActivity(), new ArrayList<>());

        listView.setAdapter(adapter);


        firestore = FirebaseFirestore.getInstance();
        loadStudentData();
        btn_Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadStudentData();
            }
        });

        btn_QRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        //Toast.makeText(getActivity(),isChecked+"",Toast.LENGTH_LONG).show();


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                // Lấy dữ liệu của item được chọn từ Adapter
//                User selectedUser = (User) adapter.getItem(position);
//
//                // Kiểm tra xem dữ liệu có tồn tại không
//                if (selectedUser != null) {
//                    // Lấy thông tin cụ thể của item
//
//                    boolean isChecked = selectedUser.isXacthuc();
//                    Toast.makeText(getActivity(),isChecked+"",Toast.LENGTH_LONG).show();
//                }
//            }
//        });


        return view;
    }

    private void loadStudentData() {
        firestore.collection("users")
                .whereEqualTo("role", "student")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<User> students = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Chỉ lấy các trường dữ liệu cần thiết từ tài liệu
                        String name = documentSnapshot.getString("name");
                        String ID = documentSnapshot.getString("MSSV");
                        Boolean xacthucObject = documentSnapshot.getBoolean("diemdanh");

                        // Kiểm tra xem xacthucObject có giá trị không
                        boolean xacthuc = xacthucObject != null && xacthucObject.booleanValue();

                        // Tạo đối tượng Student từ các trường dữ liệu đã lấy
                        User student = new User(name, ID, xacthuc);
                        students.add(student);
                    }
                    adapter.clear();
                    adapter.addAll(students);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                });
    }



}