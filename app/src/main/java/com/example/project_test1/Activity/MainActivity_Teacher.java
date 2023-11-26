package com.example.project_test1.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.project_test1.Fragment.Teacher.HomeFragment_Teacher;
import com.example.project_test1.Fragment.Teacher.LibraryFragment_Teacher;
import com.example.project_test1.Fragment.Teacher.ShortsFragment_Teacher;
import com.example.project_test1.Fragment.Teacher.SubscriptionsFragment_Teacher;
import com.example.project_test1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity_Teacher extends AppCompatActivity {

    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,new HomeFragment_Teacher()).commit();
            //navigationView.setCheckedItem(R.id.nav_view);
        }
        navigationView.setClickable(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                boolean check = true;
                if (menuItem.getItemId() == R.id.nav_home)
                {
                    Log.d("Check Click","OK");
                    Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                }
                else if (menuItem.getItemId() == R.id.nav_settings)
                {
                    Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();

                }
                else if (menuItem.getItemId() == R.id.nav_share)
                {
                    Toast.makeText(getApplicationContext(),"Share",Toast.LENGTH_SHORT).show();

                }
                else if (menuItem.getItemId() == R.id.nav_about)
                {
                    Toast.makeText(getApplicationContext(),"About us",Toast.LENGTH_SHORT).show();

                }
                else if (menuItem.getItemId() == R.id.nav_logout)
                {
                    Toast.makeText(getApplicationContext(),"Log out",Toast.LENGTH_SHORT).show();

                }
                else
                {

                }
                return true;
            }
        });


        replaceFragment(new HomeFragment_Teacher());

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.home)
            {
                replaceFragment(new HomeFragment_Teacher());
            } else if (item.getItemId() == R.id.shorts) {
                replaceFragment(new ShortsFragment_Teacher());
            }
            else if (item.getItemId() == R.id.subscriptions)
            {
                replaceFragment(new SubscriptionsFragment_Teacher());
            }
            else if(item.getItemId() == R.id.library)
            {
                replaceFragment(new LibraryFragment_Teacher());
            }


            return true;
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });

    }
    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
        LinearLayout liveLayout = dialog.findViewById(R.id.layoutLive);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                Intent intent = new Intent(getApplicationContext(),Mark_Activity.class);
                startActivity(intent);
                Toast.makeText(MainActivity_Teacher.this,"Upload Mark is clicked",Toast.LENGTH_SHORT).show();

            }
        });

        shortsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity_Teacher.this,"Create a short is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        liveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity_Teacher.this,"Go live is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}