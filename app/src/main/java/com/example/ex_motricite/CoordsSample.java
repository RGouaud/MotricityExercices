package com.example.ex_motricite;

public class CoordsSample {
    private String c_X;
    private String c_Y;
    private String temps;

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

    public String getTemps() {
        return temps;
    }

    public void setTemps(String temps) {
        this.temps = temps;
    }

    @Override
    public String toString() {
        return "CoordsSample{" +
                "c_X='" + c_X + '\'' +
                ", c_Y='" + c_Y + '\'' +
                ", temps='" + temps + '\'' +
                '}';
    }
}
