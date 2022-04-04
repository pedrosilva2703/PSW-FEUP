package GUI;

import Controllers.userController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.user;

import java.io.IOException;
import java.util.ArrayList;

public class changeUsernamePopUp {

    private static user currentUser;

    @FXML private TextField currentUsername;

    @FXML private TextField newUsername;

    protected static void setCurrentUser(user user){
        currentUser=user;
    }

    void changeUsernameWindow (ActionEvent event){

        try{
            Parent root = FXMLLoader.load(getClass().getResource("changeusernamepopup.fxml"));
            Scene scene = new Scene(root);

            Stage primaryStage = new Stage();
            primaryStage.setResizable(false);
            primaryStage.setTitle("Change Username");
            primaryStage.setScene(scene);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.showAndWait();
        }catch(IOException e){
            System.out.println(e);
        }

    }

    @FXML
    void updateUsername(ActionEvent event){

        if(currentUsername.getText().isEmpty() || newUsername.getText().isEmpty()){
            alerts alert = new alerts();
            alert.alertBox("Error", "The fields can't be empty.");
            return;
        }

        if(currentUser.getUsername().equals(currentUsername.getText())){
            Main m = new Main();

            ArrayList<user> allUsers = m.getWarehouseController().getAllUsers();
            for(user current: allUsers ){
                if(current.getUsername().equals(newUsername.getText())){
                    NotificationBox.display("Error", "New username already in use.");
                return;
                }
            }

            m.getWarehouseController().updateUsernameWHandDB(currentUser.getUsername(),newUsername.getText());
            userController userController= new userController(currentUser);
            userController.changeUsername(newUsername.getText());


            NotificationBox.display("Success","Username updated successfully!");
            return;
        }else{
            NotificationBox.display("Error","The actual username don't match your real username.");
            return;
        }

    }




}
