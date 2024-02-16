package com.example.ex_motricite;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DeletedTestTest {
    @Test
    void id_are_correctly_inserted_in_DeletedTest() {
        // GIVEN
        int id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        com.example.ex_motricite.DeletedTest test = new DeletedTest(id, path, suppressionDate);
        // WHEN
        int id1 = test.getId();
        // THEN
        assertEquals(id1, id);
    }

    @Test
    void test_set_id_correctly_apply() {
        // GIVEN
        int id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        com.example.ex_motricite.DeletedTest test = new DeletedTest(id, path, suppressionDate);
        // WHEN
        int id1 = 2;
        test.setId(id1);
        // THEN
        assertEquals(id1, test.getId());
    }

    @Test
    void test_path_is_correctly_retrieved() {
        // GIVEN
        int id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        com.example.ex_motricite.DeletedTest test = new DeletedTest(id, path, suppressionDate);
        // WHEN
        String retrievedPath = test.getPath();
        // THEN
        assertEquals(retrievedPath, path);
    }

    @Test
    void suppression_date_are_correctly_inserted_in_the_test() {
        // GIVEN
        int id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        com.example.ex_motricite.DeletedTest test = new DeletedTest(id, path, suppressionDate);
        // WHEN
        String suppressionDate1 = test.getSuppressionDate();
        // THEN
        assertEquals(suppressionDate1, suppressionDate);
    }

    @Test
    void test_set_suppression_date_correctly_apply() {
        // GIVEN
        int id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        com.example.ex_motricite.DeletedTest test = new DeletedTest(id, path, suppressionDate);
        // WHEN
        String suppressionDate1 = "02/01/2000";
        test.setSuppressionDate(suppressionDate1);
        // THEN
        assertEquals(suppressionDate1, test.getSuppressionDate());
    }

    @Test
    void test_to_string_is_correct() {
        // GIVEN
        int id = 1;
        String path = "/directory/file";
        String suppressionDate = "01/01/2000";
        com.example.ex_motricite.DeletedTest test = new DeletedTest(id, path, suppressionDate);
        // WHEN
        String expected = "Test{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", dateSuppression='" + suppressionDate + '\'' +
                '}';
        // THEN
        assertEquals(expected, test.toString());
    }

}