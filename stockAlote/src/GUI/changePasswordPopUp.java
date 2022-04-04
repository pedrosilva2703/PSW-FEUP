package GUI;

import Controllers.userController;
import cli.encrypter;
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

public class changePasswordPopUp {

    private static user currentUser;


    @FXML private TextField confirmPW;
    @FXML private TextField currentPW;
    @FXML private TextField newPW;


    protected static void setCurrentUser(user user){
        currentUser=user;
    }

    void changePasswordWindow (ActionEvent event){

        try{
            Parent root = FXMLLoader.load(getClass().getResource("changepasswordpopup.fxml"));
            Scene scene = new Scene(root);

            Stage primaryStage = new Stage();
            primaryStage.setResizable(false);
            primaryStage.setTitle("Change Password");
            primaryStage.setScene(scene);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.showAndWait();
        }catch(IOException e){
            System.out.println(e);
        }

    }

    @FXML
    void updatePassword(ActionEvent event){


        if(currentPW.getText().isEmpty() || confirmPW.getText().isEmpty() || newPW.getText().isEmpty()){
            alerts alert = new alerts();
            alert.alertBox("Error", "The fields can't be empty.");
            return;
        }

        encrypter encrypter = new encrypter();

        if(!encrypter.validatePassword(currentPW.getText(),currentUser.getPassword())) {
            NotificationBox.display("Error", "The actual password doesn't match.");
            return;
        }

       if(newPW.getText().equals(confirmPW.getText())) {
           Main m = new Main();
           String newHashPw= encrypter.generateStrongPasswordHash(newPW.getText());
           m.getWarehouseController().updatePasswordWHandDB(currentUser,newHashPw);

           userController userController= new userController(currentUser);
           userController.changePassword(newHashPw);

           NotificationBox.display("Success","Password updated successfully!");
       }else{
            NotificationBox.display("Error","The new password doesn't match.");
       }
       return;

    }




}
