package Controllers;

import cli.encrypter;
import db.SQL;
import logic.user;
import logic.warehouse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class warehouseControllerTest {
    private static warehouse wh;
    private static SQL db;
    private static warehouseController whController;
    private static user newUser;

    @BeforeAll
    static void setUp() {
        wh = new warehouse();
        db = new SQL();
        whController=new warehouseController(wh,db);
        newUser = new user();
        newUser.setUsername("Jeremias");

        newUser.setPassword(new encrypter().generateStrongPasswordHash("12345"));

        whController.addUser(newUser);
    }

    @Test
    void checkUserPass() {
        try {
            Assertions.assertTrue(whController.checkUsername("Jeremias"));
        }catch (Exception e){
            fail("Failed Happy Pass");
        }
    }

    @Test
    void checkUserFail() {
        try {
            Assertions.assertFalse(whController.checkUsername("Antonio"));
        }catch (Exception e){
            fail("Failed Fail test");
        }
    }

    @Test
    void checkPwPass() {
        try {
            Assertions.assertTrue(whController.checkPassword("12345"));
        }catch (Exception e){
            fail("Failed Fail test");
        }
    }

    @Test
    void checkPwFail() {
        try {
            Assertions.assertFalse(whController.checkPassword("1234"));
        }catch (Exception e){
            fail("Failed Fail test");
        }
    }
}