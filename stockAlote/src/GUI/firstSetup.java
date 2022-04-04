package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class firstSetup {

    @FXML
    private TextField addressWH;

    @FXML
    private DatePicker birthdate;

    @FXML
    private TextField capacityWH;

    @FXML
    private TextField contactField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField nameWH;

    @FXML
    private PasswordField pwField;

    @FXML
    private TextField usernameField;

    @FXML
    void goToHomePage(ActionEvent event){
        Main m = new Main();
        m.changeScene("homepage.fxml");
    }

    @FXML
    void create(ActionEvent event){
        Main m = new Main();
        int capacity = 0;

        // Verify warehouse values
        // If warehouse & boss text boxes are empty
        if(nameWH.getText().isEmpty()|| addressWH.getText().isEmpty() || capacityWH.getText().isEmpty() || nameField.getText().isEmpty()
                || contactField.getText().isEmpty() || usernameField.getText().isEmpty() || pwField.getText().isEmpty()
                || birthdate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).isEmpty()) {

            alerts alert = new alerts();
            alert.alertBox("Error","The fields must not be empty!");
        }

        // If warehouse & boss text boxes are all filled up
        else{
            try {
                capacity = Integer.parseInt(capacityWH.getText());
                m.getBossController().newWareHouse(Arrays.asList(
                        nameWH.getText(),
                        addressWH.getText(),
                        capacity
                ));

                m.getWarehouseController().addUser2WH(Arrays.asList(
                        nameField.getText(),
                        contactField.getText(),
                        birthdate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        usernameField.getText(),
                        pwField.getText(),
                        "boss"
                ));

                // Define Database as NOT EMPTY and change scene to homepage
                m.setDbEmpty(false);
                m.changeScene("homepage.fxml");

            } catch (NumberFormatException e) {
                alerts alert = new alerts();
                alert.alertBox("Error","Capacity must be an integer value!");
            }

        }

    }

}
