package com.example.project_test1.models;

public class Attendance {
    private String date;
    private boolean isPresent;
    private boolean isAttendanced;

    public Attendance() {
        // Required empty public constructor for Firestore
    }

    public Attendance(String date, boolean isPresent) {
        this.date = date;
        this.isPresent = isPresent;
    }

    public Attendance(String date, boolean isPresent, boolean isAttendanced) {
        this.date = date;
        this.isPresent = isPresent;
        this.isAttendanced = isAttendanced;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public boolean isAttendanced() {
        return isAttendanced;
    }

    public void setAttendanced(boolean attendanced) {
        isAttendanced = attendanced;
    }
}
