package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import logic.user;

import java.util.Arrays;
import java.util.List;

public class login {

    @FXML
    private PasswordField pwField;

    @FXML
    private TextField usernameField;

    @FXML
    private Label wrongLogIn;

    @FXML
    void enterPressed (KeyEvent event){

        if(event.getCode () == KeyCode.ENTER){
            checkLogin();
        }
    }


    public void buttonClicked(ActionEvent event){
        checkLogin();
    }

    private void checkLogin(){
        Main m = new Main();

        // If text boxes aren't all filled
        if (usernameField.getText().isEmpty() || pwField.getText().isEmpty()) {
            alerts alert = new alerts();
            alert.alertBox("Error","The fields must not be empty!");
        }

        // If every text box has text
        else {
            List<Object> credential = Arrays.asList(usernameField.getText(),pwField.getText());
            user logged = m.getWarehouseController().checkLogin(credential);

            // Verify login credentials
            if(logged==null){
                this.wrongLogIn.setText("Credentials are incorrect");
            }
            else{
                m.setCurrentUser(logged);

                // Choose type of user
                switch (logged.getRole()){
                    case "boss":
                        m.changeScene("bosspage.fxml");
                        break;

                    case "manager":
                        m.changeScene("managerpage.fxml");
                        break;

                    case "employee":
                        m.changeScene("employeepage.fxml");
                        break;


                    default:
                        break;

                }
            }
        }
    }
}
