package com.example.project_test1.Helper;


public class ListData {
    String name, time;
    int ingredients, desc;
    int image;

    public ListData(String name, String time, int ingredients, int desc, int image) {
        this.name = name;
        this.time = time;
        this.ingredients = ingredients;
        this.desc = desc;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIngredients() {
        return ingredients;
    }

    public void setIngredients(int ingredients) {
        this.ingredients = ingredients;
    }

    public int getDesc() {
        return desc;
    }

    public void setDesc(int desc) {
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
