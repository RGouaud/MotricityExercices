package com.example.ex_motricite;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DeletedTestTest {
    @Test
    //constructor
    void testDeletedTestWhenAllParamsGiven() {
        //GIVEN
        long id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        //WHEN
        com.example.ex_motricite.DeletedTest test = new DeletedTest(id, path, suppressionDate);
        // THEN
        assertEquals("Test{id=" + id + ", path='" + path + '\'' + ", dateSuppression='" + suppressionDate + '\'' + '}', test.toString());
    }

    @Test
    void id_are_correctly_set() {
        // GIVEN
        long id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        com.example.ex_motricite.DeletedTest test = new DeletedTest(id, path, suppressionDate);
        // WHEN
        long retrievedId = test.getId();
        // THEN
        assertEquals(retrievedId, id);
    }

    @Test
    void test_set_id_correctly_set() {
        // GIVEN
        long id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        com.example.ex_motricite.DeletedTest test = new DeletedTest(id, path, suppressionDate);
        long newId = 2;
        // WHEN
        test.setId(newId);
        // THEN
        assertEquals(newId, test.getId());
    }

    @Test
    void test_path_is_correctly_retrieved() {
        // GIVEN
        long id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        com.example.ex_motricite.DeletedTest test = new DeletedTest(id, path, suppressionDate);
        // WHEN
        String retrievedPath = test.getPath();
        // THEN
        assertEquals(retrievedPath, path);
    }

    @Test
    void suppression_date_are_correctly_retrieved() {
        // GIVEN
        long id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        com.example.ex_motricite.DeletedTest test = new DeletedTest(id, path, suppressionDate);
        // WHEN
        String retrievedDate = test.getSuppressionDate();
        // THEN
        assertEquals(retrievedDate, suppressionDate);
    }

    @Test
    void test_set_suppression_date_correctly_apply() {
        // GIVEN
        long id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        com.example.ex_motricite.DeletedTest test = new DeletedTest(id, path, suppressionDate);
        String newSuppressionDate = "02/01/2000";
        // WHEN
        test.setSuppressionDate(newSuppressionDate);
        // THEN
        assertEquals(newSuppressionDate, test.getSuppressionDate());
    }
}