package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class employeePage implements Initializable {

    @FXML private Label showUsername;
    @FXML private ComboBox<String> personalStuff;

    // Button presses
    public void tasksButtonPressed (ActionEvent event){
        goToTasksScene();
    }
    public void logoutButtonPressed(ActionEvent event){
        goToHomepageScene();
    }


    // New scenes

    void goToTasksScene(){
        Main m = new Main();
        m.changeScene("employeepagetasks.fxml");
    }
    void goToHomepageScene(){
        Main m = new Main();
        m.changeScene("homepage.fxml");
    }

    @FXML
    void personalStuffPressed(ActionEvent event){
        Main m = new Main();


        //Change username
        if(personalStuff.getValue().equals(personalStuff.getItems().get(1))){
            changeUsername(event);
            personalStuff.setValue(" ");
            showUsername.setText("Welcome "+ m.getCurrentUser().getUsername());


        }// change password
        else if(personalStuff.getValue().equals(personalStuff.getItems().get(2))){
            changepassword(event);
            personalStuff.setValue(" ");
        }
    }
    private void changeUsername(ActionEvent event){
        Main m = new Main();


        changeUsernamePopUp cUserName= new changeUsernamePopUp();
        cUserName.setCurrentUser(m.getCurrentUser());
        cUserName.changeUsernameWindow(event);

    }
    private void changepassword(ActionEvent event){
        Main m = new Main();


        changePasswordPopUp cPW= new changePasswordPopUp();
        cPW.setCurrentUser(m.getCurrentUser());
        cPW.changePasswordWindow(event);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Main m = new Main();


        showUsername.setText("Welcome " + m.getCurrentUser().getUsername());
        personalStuff.getItems().addAll(" ","Change username","Change password");
        personalStuff.setValue(" ");


    }

}

