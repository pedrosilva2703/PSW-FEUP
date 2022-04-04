package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.user;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ResourceBundle;

public class bossPageUser implements Initializable {

    ObservableList<String> roleBoxList = FXCollections.observableArrayList("manager","employee");

    @FXML
    private TableView<user> userTable;

    @FXML private TableColumn<user, String> t_birthdate;
    @FXML private TableColumn<user, String> t_contact;
    @FXML private TableColumn<user, String> t_name;
    @FXML private TableColumn<user, String> t_role;
    @FXML private TableColumn<user, String> t_username;

    @FXML private Label removeUserResponse;
    @FXML private TextField usernameRemove;
    @FXML private Label showUsername;
    @FXML private Label adduserResponse;
    @FXML private TextField contactField;
    @FXML private TextField nameField;
    @FXML private DatePicker birthdate;
    @FXML private ChoiceBox<String> roleBox;
    @FXML private PasswordField pwField;
    @FXML private ListView<String> userList;
    @FXML private TextField usernameField;
    @FXML private ComboBox<String> personalStuff;

    // Button presses
    public void categoryButtonPressed(ActionEvent event){
        goToCategoryScene();
    }
    public void logoutButtonPressed(ActionEvent event){
        goToHomepageScene();
    }
    public void addButtonPressed(ActionEvent event){

        if(nameField.getText().isEmpty()|| contactField.getText().isEmpty() || (birthdate.getValue()==null)
           || usernameField.getText().isEmpty() || pwField.getText().isEmpty() ){

            alerts alert = new alerts();
            alert.alertBox("Error","The fields can not be empty!");
        }
        else{
            addNewUser();
        }
    }
    public void removeButtonPressed(ActionEvent event){
        removeUser();
    }
    public void refreshButtonPressed(ActionEvent event){
        tableRefresh();
        Main m = new Main();
        m.changeScene("bosspageuser.fxml");
    }

    // New scenes
    private void goToCategoryScene(){
        Main m = new Main();
        m.changeScene("bosspagecategory.fxml");
    }
    private void goToHomepageScene(){
        Main m = new Main();
        m.changeScene("homepage.fxml");
    }

    @FXML
    void personalStuffPressed(ActionEvent event){
        Main m = new Main();
        m.refreshLocalFromDB();

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
        m.refreshLocalFromDB();

        changeUsernamePopUp cUserName= new changeUsernamePopUp();
        cUserName.setCurrentUser(m.getCurrentUser());
        cUserName.changeUsernameWindow(event);

    }
    private void changepassword(ActionEvent event){
        Main m = new Main();
        m.refreshLocalFromDB();

        changePasswordPopUp cPW= new changePasswordPopUp();
        cPW.setCurrentUser(m.getCurrentUser());
        cPW.changePasswordWindow(event);
    }


    private void addNewUser(){
        Main m = new Main();
        m.refreshLocalFromDB();

        int result = m.getBossController().addUserFromGUI(Arrays.asList(
                nameField.getText(),
                contactField.getText(),
                birthdate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                usernameField.getText(),
                pwField.getText(),
                roleBox.getValue()
                )

        );

        if(result == 1){
            //adduserResponse.setText("User Added !");
            NotificationBox.display("Sucess","User Added !");
            userTable.setItems(m.getWarehouseController().getUserObList());
        }else if (result==-1){
            //adduserResponse.setText("User already exists !");
            NotificationBox.display("Error","User already exists !");
        }
    }
    private void removeUser(){
        // If text box is empty
        if (usernameRemove.getText().isEmpty()) {
            alerts alert = new alerts();
            alert.alertBox("Error","The fields must not be empty!");
        }
        // If text box is filled
        else {
            Main m = new Main();
            m.refreshLocalFromDB();

            int result;

            result = m.getBossController().removeUserFromGUI(usernameRemove.getText());

            if(result == 0){
                //removeUserResponse.setText("User removed!");
                NotificationBox.display("Sucess","User Removed !");
                userTable.setItems(m.getWarehouseController().getUserObList());
            }else if (result==-1){
                NotificationBox.display("Error","User don't exist !");
                //removeUserResponse.setText("User doesn't exist!");
            } else {
                NotificationBox.display("Error","You can´t fire yourself !");
                //removeUserResponse.setText("You can´t fire yourself xDDDDD");
            }
        }
    }

    private void tableRefresh () {
        Main m = new Main();
        m.refreshLocalFromDB();
        userTable.setItems(m.getWarehouseController().getUserObList());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Main m = new Main();
        m.refreshLocalFromDB();

        showUsername.setText("Welcome "+ m.getCurrentUser().getUsername());
        personalStuff.getItems().addAll(" ","Change username","Change password");
        personalStuff.setValue(" ");



        roleBox.setValue("manager");
        roleBox.setItems(roleBoxList);

        t_role.setCellValueFactory(new PropertyValueFactory<user, String>("role") );
        t_name.setCellValueFactory(new PropertyValueFactory<user, String>("name") );
        t_username.setCellValueFactory(new PropertyValueFactory<user, String>("username") );
        t_contact.setCellValueFactory(new PropertyValueFactory<user, String>("contact") );
        t_birthdate.setCellValueFactory(new PropertyValueFactory<user, String>("birthdate") );


        userTable.setItems(m.getWarehouseController().getUserObList());

    }



}
