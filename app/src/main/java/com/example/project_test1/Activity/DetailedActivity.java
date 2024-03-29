package com.example.project_test1.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.project_test1.R;
import com.example.project_test1.databinding.ActivityDetailedBinding;

public class DetailedActivity extends AppCompatActivity {

    ActivityDetailedBinding binding;
    ImageView detailImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        if (intent != null){
            String name = intent.getStringExtra("name");
            String ID = intent.getStringExtra("ID");
            String lop = intent.getStringExtra("lop");
            //int desc = intent.getIntExtra("desc", R.string.maggieDesc);
            int image = intent.getIntExtra("image", R.drawable.pasta);

            detailImage = (ImageView) findViewById(R.id.detailImage);
            String Image_URL = intent.getStringExtra("Image_URL");

            binding.detailName.setText(name);
            binding.detailID.setText(ID);
//            binding.detailDesc.setText(desc);
//            binding.detailIngredients.setText(ingredients);
            //binding.detailImage.setImageResource(image);
            Glide.with(this)
                    .load(Image_URL)
                    .into(binding.detailImage);
        }
    }
}