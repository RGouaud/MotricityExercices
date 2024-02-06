package com.example.ex_motricite;

public class Test {
    private int id;
    private String path;
    private String suppressionDate;

    public Test(int id, String path, String suppressionDate) {
        this.id = id;
        this.path = path;
        this.suppressionDate = suppressionDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSuppressionDate() {
        return suppressionDate;
    }

    public void setSuppressionDate(String suppressionDate) {
        this.suppressionDate = suppressionDate;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", dateSuppression='" + suppressionDate + '\'' +
                '}';
    }
}
