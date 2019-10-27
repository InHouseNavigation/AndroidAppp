package com.syntaxerror.naviapp;

public class ListenKategories {
    private String name;
    private int drawable;

    public ListenKategories(String name, int drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public int getImgURL() {
        return drawable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgURL(int drawable) {
        this.drawable = drawable;
    }

}
