package com.example.project_test1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.project_test1.R;
import com.example.project_test1.models.Attendance;

import java.util.ArrayList;

public class WeekAdapter extends ArrayAdapter<Attendance> {
    public WeekAdapter(Context context, ArrayList<Attendance> Attendance) {
        super(context, 0, Attendance);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Attendance attendance = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_week, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.nameWeek);
        TextView idTextView = convertView.findViewById(R.id.nameDate);


        nameTextView.setText("Tuần" + Integer.toString(attendance.getWeek()));
        idTextView.setText(attendance.getDate());


        return convertView;
    }
}
