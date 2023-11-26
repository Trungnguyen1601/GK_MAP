package com.example.project_test1.models;

public class Lesson {
    private int week;
    private int SL_SV ;
    private int SL_SV_Diemdanh;


    public Lesson() {
    }

    public Lesson(int week, int SL_SV, int SL_SV_Diemdanh) {
        this.week = week;
        this.SL_SV = SL_SV;
        this.SL_SV_Diemdanh = SL_SV_Diemdanh;

    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getSL_SV() {
        return SL_SV;
    }

    public void setSL_SV(int SL_SV) {
        this.SL_SV = SL_SV;
    }

    public int getSL_SV_Diemdanh() {
        return SL_SV_Diemdanh;
    }

    public void setSL_SV_Diemdanh(int SL_SV_Diemdanh) {
        this.SL_SV_Diemdanh = SL_SV_Diemdanh;
    }


}
