package com.example.project_test1.models;

public class User {
    private String email;
    private String password;
    private String MSSV;

    private String Lop;
    private String name;
    private String ID;
    private boolean xacthuc;
    private String image_URL;
    private Boolean permission;
    private String Image;

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

    public User(String lop, String name, String ID, boolean xacthuc, String image_URL) {
        Lop = lop;
        this.name = name;
        this.ID = ID;
        this.xacthuc = xacthuc;
        this.image_URL = image_URL;
    }

    public User(String email, String password, String MSSV,String lop, String name, String ID, boolean xacthuc, String image_URL, Boolean permission, String image) {
        this.email = email;
        this.MSSV = MSSV;
        this.password = password;
        Lop = lop;
        this.name = name;
        this.ID = ID;
        this.xacthuc = xacthuc;
        this.image_URL = image_URL;
        this.permission = permission;
        Image = image;
    }

    public User(String MSSV, String lop, String name, String image) {
        this.MSSV = MSSV;
        Lop = lop;
        this.name = name;
        Image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImage_URL() {
        return image_URL;
    }

    public void setImage_URL(String image_URL) {
        this.image_URL = image_URL;
    }

    public Boolean getPermission() {
        return permission;
    }

    public void setPermission(Boolean permission) {
        this.permission = permission;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getMSSV() {
        return MSSV;
    }

    public void setMSSV(String MSSV) {
        this.MSSV = MSSV;
    }
}
