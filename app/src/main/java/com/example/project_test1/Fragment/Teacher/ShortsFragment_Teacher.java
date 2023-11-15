package com.example.project_test1.Fragment.Teacher;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.project_test1.Helper.FireStore_Help;
import com.example.project_test1.R;
import com.example.project_test1.models.User;
import com.example.project_test1.models.UserAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
    private static final int REQUEST_CODE = 100;
    private Bitmap bitmap;
    private static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    // Cho hiển thị ngày
    private PopupWindow popupWindow;
    private Button ChooseDateButton;
    private boolean isListVisible = false;
    private String selectedDate = "Default"; // Tên mặc định cho nút

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
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode("Ngay 10/10/2023", BarcodeFormat.QR_CODE,300,300);

                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    bitmap = barcodeEncoder.createBitmap(bitMatrix);


                    // Lưu hình ảnh vào thư viện điện thoại
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) {
                            saveImageToGallery(bitmap);
                        } else {
                            requestPermissions(PERMISSIONS, REQUEST_CODE);
                        }
                    } else {
                        saveImageToGallery(bitmap);
                    }

                } catch (WriterException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        ChooseDateButton = (Button)view.findViewById(R.id.DateListButton);

        ChooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isListVisible) {
                    hidePopupWindow();
                } else {
                    showPopupWindow(view);
                }
            }
        });


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
    private void saveImageToGallery(Bitmap bitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "QR Code");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        // Đường dẫn để lưu hình ảnh
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        path += "/QR Code/";

        // Tạo thư mục nếu chưa tồn tại
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Đặt tên hình ảnh
        String fileName = "QRCode_" + System.currentTimeMillis() + ".png";
        values.put(MediaStore.Images.Media.DATA, path + fileName);

        // Lưu hình ảnh vào thư viện điện thoại
        OutputStream os;
        try {
            Uri uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            os = getContext().getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
            Toast.makeText(getActivity(), "Hình ảnh đã được lưu vào thư viện", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (bitmap != null) {
                    saveImageToGallery(bitmap); // Lưu hình ảnh vào thư viện điện thoại
                } else {
                    Toast.makeText(getActivity(), "Không có hình ảnh để lưu", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Bạn cần cấp quyền để lưu hình ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showPopupWindow(View view) {
        //LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        View popupView = inflater.inflate(R.layout.listdate_layout, null);

        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Set up the list
        ListView listView = popupView.findViewById(R.id.listView);
        List<String> items = new ArrayList<>();
        LocalDate dateAttendance = LocalDate.of(2023, 10, 9); // Thay thế bằng ngày bắt đầu mong muốn
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (int start = 1; start < 15; start ++)
        {
            items.add(dateAttendance.format(formatter).toString());
            // Lấy ngày cách nhau 1 tuần
            dateAttendance = dateAttendance.plus(1, ChronoUnit.WEEKS);
        }





        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        // Set item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click here
                // For example, you can close the PopupWindow and do something with the selected item
                selectedDate = items.get(position);
                Update_Date_Attention(firestore,selectedDate);
                updateButtonLabel(ChooseDateButton,selectedDate);
                hidePopupWindow();
            }
        });

        // Show the PopupWindow at the center of the button
        popupWindow.showAsDropDown(view, 0, 0);
        isListVisible = true;
    }

    private void hidePopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            isListVisible = false;
        }
    }

    private void updateButtonLabel(Button button, String nameDate) {
        button.setText(nameDate);
    }

    private void Update_Date_Attention(FirebaseFirestore firestore, String selectedDate)
    {
        FireStore_Help.Update_All_Documment_String(firestore,"users","Ngay_diemdanh",selectedDate);

    }
}