package com.example.project_test1.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_test1.Activity.DetailedActivity;
import com.example.project_test1.R;
import com.example.project_test1.models.User;

import java.util.List;

public class StudentAcceptPermissionAdapter extends RecyclerView.Adapter<StudentAcceptPermissionAdapter.StudentViewHolder> {

    private List<User> userList; // Danh sách sinh viên

    public StudentAcceptPermissionAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_accept_item, parent, false);
        return new StudentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewStudentName.setText(user.getName());
        holder.textViewStudentMSSV.setText(user.getMSSV());
        holder.buttonAccept.setOnClickListener(v -> {
            //Toast.makeText(v.getContext(),"Hello" + user.getName(),Toast.LENGTH_SHORT).show();
            // Xử lý sự kiện khi người dùng nhấn vào nút "Accept"
            // Ở đây có thể thêm mã để chấp nhận sinh viên vào lớp
            // Ví dụ: hiển thị thông báo, thay đổi trạng thái của sinh viên, vv.
        });
        holder.itemView.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.item_click_animation);
            holder.itemView.startAnimation(animation);
            //Toast.makeText(v.getContext(),"Hello Trung" + user.getName(),Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), DetailedActivity.class);

            // Xử lý sự kiện khi người dùng nhấn vào sinh viên
            // Ở đây, bạn có thể hiển thị thông tin chi tiết của sinh viên nếu cần
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStudentName;
        Button buttonAccept;
        TextView textViewStudentMSSV;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStudentName = itemView.findViewById(R.id.textViewStudentName);
            buttonAccept = itemView.findViewById(R.id.buttonAccept);
            textViewStudentMSSV = itemView.findViewById(R.id.textViewStudentMSSV);

        }
    }
}

