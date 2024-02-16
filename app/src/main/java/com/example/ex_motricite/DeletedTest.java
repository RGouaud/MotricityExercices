package com.example.ex_motricite;

import androidx.annotation.NonNull;

public class DeletedTest {
    private long id;
    private final String path;
    private String suppressionDate;

    public DeletedTest(long id, String path, String suppressionDate) {
        this.id = id;
        this.path = path;
        this.suppressionDate = suppressionDate;
    }

    public DeletedTest(String path, String suppressionDate){
        this.path = path;
        this.suppressionDate = suppressionDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }



    public String getSuppressionDate() {
        return suppressionDate;
    }

    public void setSuppressionDate(String suppressionDate) {
        this.suppressionDate = suppressionDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", dateSuppression='" + suppressionDate + '\'' +
                '}';
    }
}
