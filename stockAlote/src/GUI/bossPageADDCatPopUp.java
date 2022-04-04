package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;



public class bossPageADDCatPopUp {

    @FXML
    private TextField newCategoryCapField;

    @FXML
    private TextField newCategoryField;


    void addNewCatWindow (ActionEvent event){

        try{
            Parent root = FXMLLoader.load(getClass().getResource("newCatPopUp.fxml"));
            Scene scene = new Scene(root);

            Stage primaryStage = new Stage();
            primaryStage.setResizable(false);
            primaryStage.setTitle("Add Category");
            primaryStage.setScene(scene);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.showAndWait();
        }catch(IOException e){
            System.out.println(e);
        }

    }
    @FXML
    void addNewCategory(){
        Main m = new Main();
        m.refreshLocalFromDB();

        // If any text box is empty
        if (newCategoryField.getText().isEmpty() || newCategoryCapField.getText().isEmpty()) {
            alerts alert = new alerts();
            alert.alertBox("Error","The fields must not be empty!");
        }

        // If all text boxes are filled
        else {
            // Detect if capacity is an integer
            try {
                // Creates new category with text box parameters
                int response = m.getBossController().newCategory(newCategoryField.getText(),
                        Integer.parseInt(newCategoryCapField.getText()));

                if(response >= 0) {
                    NotificationBox.display("Success", "Section Added !");
                }
                else if (response==-2) NotificationBox.display("Error","Not enough space !");
                else NotificationBox.display("Error","Already exists !");

            } catch (NumberFormatException e) {
                alerts alert = new alerts();
                alert.alertBox("Error","Capacity must be an integer!");
            }
        }

    }

}
