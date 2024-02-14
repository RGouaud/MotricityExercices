package com.example.ex_motricite;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class TestTest {

    @Test
    public void id_are_correctly_inserted_in_the_test() {
        // GIVEN
        int id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        com.example.ex_motricite.Test test = new com.example.ex_motricite.Test(id, path, suppressionDate);
        // WHEN
        int id1 = test.getId();
        // THEN
        assertEquals(id1, id);
    }

    @Test
    public void test_set_id_correctly_apply() {
        // GIVEN
        int id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        com.example.ex_motricite.Test test = new com.example.ex_motricite.Test(id, path, suppressionDate);
        // WHEN
        int id1 = 2;
        test.setId(id1);
        // THEN
        assertEquals(id1, test.getId());
    }

    @Test
    public void getPath() {
        // GIVEN
        int id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        com.example.ex_motricite.Test test = new com.example.ex_motricite.Test(id, path, suppressionDate);
        // WHEN
        String path1 = test.getSuppressionDate();
        // THEN
        assertEquals(path1, path);
    }

    @Test
    public void suppression_date_are_correctly_inserted_in_the_test() {
        // GIVEN
        int id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        com.example.ex_motricite.Test test = new com.example.ex_motricite.Test(id, path, suppressionDate);
        // WHEN
        String suppressionDate1 = test.getSuppressionDate();
        // THEN
        assertEquals(suppressionDate1, suppressionDate);
    }

    @Test
    public void test_set_suppression_date_correctly_apply() {
        // GIVEN
        int id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        com.example.ex_motricite.Test test = new com.example.ex_motricite.Test(id, path, suppressionDate);
        // WHEN
        String suppressionDate1 = "02/01/2000";
        test.setSuppressionDate(suppressionDate1);
        // THEN
        assertEquals(suppressionDate1, test.getSuppressionDate());
    }

    @Test
    public void testToString() {
    }
}