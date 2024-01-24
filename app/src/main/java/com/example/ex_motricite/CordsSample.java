package com.example.ex_motricite;

import androidx.annotation.NonNull;

public class CordsSample {
    private String c_X;
    private String c_Y;
    private String time;

    public String getC_X() {
        return c_X;
    }

    public void setC_X(String c_X) {
        this.c_X = c_X;
    }

    public String getC_Y() {
        return c_Y;
    }

    public void setC_Y(String c_Y) {
        this.c_Y = c_Y;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @NonNull
    @Override
    public String toString() {
        return "CordsSample{" +
                "c_X='" + c_X + '\'' +
                ", c_Y='" + c_Y + '\'' +
                ", temps='" + time + '\'' +
                '}';
    }
}
