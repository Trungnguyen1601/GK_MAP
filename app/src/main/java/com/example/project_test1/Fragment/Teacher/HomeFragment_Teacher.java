package com.example.project_test1.Fragment.Teacher;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_test1.Activity.DetailedActivity;
import com.example.project_test1.Helper.ListAdapter;
import com.example.project_test1.Helper.ListData;
import com.example.project_test1.R;
import com.example.project_test1.databinding.FragmentHomeTeacherBinding;
import com.example.project_test1.models.User;
import com.example.project_test1.models.UserAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment_Teacher#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment_Teacher extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Project
    FragmentHomeTeacherBinding binding;
    ListAdapter listAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListData listData;

    private ListView listView;
    private ListAdapter adapter;
    private FirebaseFirestore firestore;
    public HomeFragment_Teacher() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment_Teacher newInstance(String param1, String param2) {
        HomeFragment_Teacher fragment = new HomeFragment_Teacher();
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
        binding = FragmentHomeTeacherBinding.inflate(inflater, container, false);
        //View view = inflater.inflate(R.layout.fragment_home_teacher, container, false);
        //listView = view.findViewById(R.id.listView);
        adapter = new ListAdapter(getActivity(), new ArrayList<>());

        binding.listView.setAdapter(adapter);
        binding.listView.setClickable(true);

        firestore = FirebaseFirestore.getInstance();



        loadStudentData();


        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Lấy dữ liệu của item được chọn từ Adapter
                User selectedUser = (User) adapter.getItem(position);

                // Kiểm tra xem dữ liệu có tồn tại không
                if (selectedUser != null) {
                    // Lấy thông tin cụ thể của item
                    String name = selectedUser.getName();
                    String ID = selectedUser.getID();
                    boolean xacthuc = selectedUser.isXacthuc();
                    String Lop = selectedUser.getLop();
                    String Image_URL = selectedUser.getImage_URL();

                    // Thực hiện các thao tác với dữ liệu như chuyển sang DetailedActivity
                    Intent intent = new Intent(getContext(), DetailedActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("ID", ID);
                    intent.putExtra("xacthuc", xacthuc);
                    intent.putExtra("lop",Lop);
                    intent.putExtra("Image_URL",Image_URL);
                    startActivity(intent);
                }
            }
        });
        return binding.getRoot();

    }

    private void loadStudentData() {
        firestore.collection("users")
                .whereEqualTo("role","student")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<User> students = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Chỉ lấy các trường dữ liệu cần thiết từ tài liệu
                        String name = documentSnapshot.getString("name");
                        String ID = documentSnapshot.getString("MSSV");
                        Boolean xacthucObject = documentSnapshot.getBoolean("diemdanh");
                        String Lop = documentSnapshot.getString("class");
                        String Image_URL = documentSnapshot.getString("Image_URL");


                        // Kiểm tra xem xacthucObject có giá trị không
                        boolean xacthuc = xacthucObject != null && xacthucObject.booleanValue();

                        // Tạo đối tượng Student từ các trường dữ liệu đã lấy
                        User student = new User(Lop,name, ID, xacthuc,Image_URL);
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
    // Phương thức để cập nhật UI khi có sự thay đổi
    private void updateUI() {
        // Cập nhật UI của bạn ở đây
        // Ví dụ: Hiển thị thông báo, cập nhật ListView, v.v.
        adapter.notifyDataSetChanged();
    }
}
