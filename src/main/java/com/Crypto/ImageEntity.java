package com.Crypto;

import java.util.ArrayList;

public class ImageEntity {
    private ArrayList<ArrayList<Integer>> pixels;
    int height;
    int width;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public ArrayList<ArrayList<Integer>> getPixels() {
        return pixels;
    }

    public void setPixels(ArrayList<ArrayList<Integer>> pixels) {
        this.pixels = pixels;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
