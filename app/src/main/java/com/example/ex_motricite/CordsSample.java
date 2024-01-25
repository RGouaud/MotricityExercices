package com.example.ex_motricite;

import androidx.annotation.NonNull;

public class CordsSample {
    private String cX;
    private String cY;
    private String time;

    public String getCX() {
        return cX;
    }

    public void setCX(String cX) {
        this.cX = cX;
    }

    public String getCY() {
        return cY;
    }

    public void setCY(String cY) {
        this.cY = cY;
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
                "c_X='" + cX + '\'' +
                ", c_Y='" + cY + '\'' +
                ", temps='" + time + '\'' +
                '}';
    }
}
