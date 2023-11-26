package com.example.project_test1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.project_test1.R;
import com.example.project_test1.models.Lesson;

import java.util.ArrayList;

public class LessonAdapter extends ArrayAdapter<Lesson> {
    public LessonAdapter(Context context, ArrayList<Lesson> Lessons) {
        super(context, 0, Lessons);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Lesson lesson = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_lesson, parent, false);
        }

        TextView Tuan = convertView.findViewById(R.id.tuanhoc);
        TextView Si_so = convertView.findViewById(R.id.si_so_lop);
        TextView Soluong_sinhvien = convertView.findViewById(R.id.soluongsinhvien);
        TextView Soluong_sinhvien_khongdiemdanh = convertView.findViewById(R.id.soluongsinhvien_khongdiemdanh);

        Tuan.setText(Integer.toString(lesson.getWeek()));
        Si_so.setText(Integer.toString(lesson.getSL_SV()));
        Soluong_sinhvien.setText(Integer.toString(lesson.getSL_SV_Diemdanh()));
        Soluong_sinhvien_khongdiemdanh.setText(Integer.toString(lesson.getSL_SV() - lesson.getSL_SV_Diemdanh()));



        return convertView;
    }
}