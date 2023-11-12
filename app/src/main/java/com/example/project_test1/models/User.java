package com.example.project_test1.models;

public class User {
    private String email;
    private String password;

    private String Lop;
    private String name;
    private String ID;
    private boolean xacthuc;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public User(String name, String ID, boolean xacthuc) {
        this.name = name;
        this.ID = ID;
        this.xacthuc = xacthuc;
    }

    public User(String lop, String name, String ID, boolean xacthuc) {
        Lop = lop;
        this.name = name;
        this.ID = ID;
        this.xacthuc = xacthuc;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public boolean isXacthuc() {
        return xacthuc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLop() {
        return Lop;
    }

    public void setLop(String lop) {
        Lop = lop;
    }

    public void setCheck(boolean isChecked) {
        xacthuc = isChecked;
    }
}
