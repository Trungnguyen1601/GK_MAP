package com.example.project_test1.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.project_test1.R;
import com.example.project_test1.models.User;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<User> {
    public ListAdapter(Context context, ArrayList<User> Users) {
        super(context, 0, Users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User User = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView idTextView = convertView.findViewById(R.id.idTextView);


        nameTextView.setText(User.getName());
        idTextView.setText(User.getID());


        return convertView;
    }
}
