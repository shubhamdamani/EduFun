package com.example.droidrun;

import android.graphics.Path;

public class FingerPath {

    public int color;
    public int inkWidth;
    public Path path;

    public FingerPath(int color, int strokeWidth, Path path) {
        this.color = color;
        this.inkWidth = strokeWidth;
        this.path = path;
    }
}
