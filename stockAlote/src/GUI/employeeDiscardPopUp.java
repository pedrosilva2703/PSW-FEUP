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



public class employeeDiscardPopUp  {


    @FXML private TextField categoryName;
    @FXML private TextField productName;
    @FXML private TextField quantity;

    void discardWindow(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("discardPopUp.fxml"));
            Scene scene = new Scene(root);

            Stage primaryStage = new Stage();
            primaryStage.setResizable(false);
            primaryStage.setTitle("Discard Product");
            primaryStage.setScene(scene);
            primaryStage.initModality(Modality.WINDOW_MODAL);
            primaryStage.showAndWait();
        }catch(IOException e){
            System.out.println(e);
        }
    }

    @FXML
    void discardProduct(){
        Main m = new Main();

        // If any text box is empty
        if (productName.getText().isEmpty() || categoryName.getText().isEmpty() || quantity.getText().isEmpty()) {
            alerts alert = new alerts();
            alert.alertBox("Error","The fields must not be empty!");
        }
        // If all text boxes are filled
        else {
            // Detect if capacity is an integer
            try {
                int response = m.getEmployeeController().discardDamagedProduct(
                        productName.getText(),
                        Integer.parseInt(quantity.getText()),
                        categoryName.getText()
                        );

                if(response >= 0) {
                   NotificationBox.display("Success", "Product Discarded !");
                    // Especial Task
                    m.getManagerController().createTask("discard",productName.getText(),categoryName.getText(),"---",Integer.parseInt(quantity.getText()));

                }
                else if (response==-1) {
                    NotificationBox.display("Error", "Good not found !");
                }
                else if (response==-2) {
                    NotificationBox.display("Error", "Impossible discard more than available!");
                }

           } catch (NumberFormatException e) {
                alerts alert = new alerts();
                alert.alertBox("Error","Quantity must be an integer");
            }
        }


    }

}
