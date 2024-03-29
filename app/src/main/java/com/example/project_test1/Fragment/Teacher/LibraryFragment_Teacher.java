package com.example.project_test1.Fragment.Teacher;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_test1.Activity.LoginActivity;
import com.example.project_test1.Fragment.Student.LibraryFragment_Student;
import com.example.project_test1.Helper.LocaleManager;
import com.example.project_test1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LibraryFragment_Teacher#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryFragment_Teacher extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView btnChangeLanguage;
    TextView btnLogOut;
    TextView txtUser;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;

    public LibraryFragment_Teacher() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibraryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibraryFragment_Teacher newInstance(String param1, String param2) {
        LibraryFragment_Teacher fragment = new LibraryFragment_Teacher();
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
        View view = inflater.inflate(R.layout.fragment_setting_teacher, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        btnLogOut = (TextView) view.findViewById(R.id.btn_LogOut);
        btnChangeLanguage = (ImageView) view.findViewById(R.id.buttonChangeLanguage);

//        txtUser = (TextView) view.findViewById(R.id.txtUser);
        user = firebaseAuth.getCurrentUser();

        if (user == null ) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        else {
//            txtUser.setText(user.getEmail());
        }

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

        btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LanguageChange", "Button clicked!");

                // Get the current language
                String currentLanguage = LocaleManager.getSelectedLanguage(getActivity());

                // Toggle to the other language
                String newLanguage = currentLanguage.equals("en") ? "vi" : "en";
                Log.d("LanguageChange", "New language: " + newLanguage);

                // Change language when the user clicks the button
                changeLanguage(newLanguage);

            }
        });

        return view;
    }

    private void changeLanguage(String language) {
        Log.d("LanguageChange", "Changing language to: " + language);

        // Gọi hàm để thay đổi ngôn ngữ và cập nhật layout
        updateLocale(language);

        // Update UI elements directly
        updateUIElements(language);
    }

    private void updateLocale(String language) {
        Log.d("LanguageChange", "Updating locale to: " + language);

        // Set the default locale for the entire application
        Locale newLocale = new Locale(language);
        Locale.setDefault(newLocale);

        // Update configuration
        Configuration configuration = new Configuration(getResources().getConfiguration());
        configuration.setLocale(newLocale);
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        // Make sure to set the locale for your LocaleManager if you have one
        LocaleManager.setLocale(getActivity().getApplicationContext(), language);
    }

    private void updateUIElements(String language) {
        Log.d("LanguageChange", "Updating UI elements for language: " + language);

        // Update the button image based on the current language
        if (language.equals("vi")) {
            Log.d("LanguageChange", "Setting image to flag_vietnam");
            btnChangeLanguage.setImageResource(R.drawable.flag_vietnam);
        } else {
            Log.d("LanguageChange", "Setting image to flag_us");
            btnChangeLanguage.setImageResource(R.drawable.flag_us);
        }
        // Không cần gọi btnChangeLanguage.invalidate() vì setImageResource sẽ tự làm điều này
    }
}