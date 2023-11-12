package com.example.project_test1.Fragment.Student;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_test1.Activity.LoginActivity;
import com.example.project_test1.Activity.ScanQRActivity;
import com.example.project_test1.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShortsFragment_Student#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShortsFragment_Student extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String data;
    public ShortsFragment_Student() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShortsFragment_Student.
     */
    // TODO: Rename and change types and number of parameters
    public static ShortsFragment_Student newInstance(String param1, String param2) {
        ShortsFragment_Student fragment = new ShortsFragment_Student();
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
        View view = inflater.inflate(R.layout.fragment_shorts_student, container, false);


        Bundle args = getArguments();
        if (args != null) {
            data = args.getString("key");
            TextView textView = view.findViewById(R.id.text_view);
            textView.setText(data);
            Log.d(TAG, data);
        }

        Intent intent = new Intent(getActivity(), ScanQRActivity.class);
        intent.putExtra("email_QR", data);
        startActivity(intent);
        getActivity().finish();



        return view;
    }
}