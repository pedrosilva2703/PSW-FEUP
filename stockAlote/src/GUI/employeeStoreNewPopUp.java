package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class employeeStoreNewPopUp implements Initializable {

    private static String name;
    private static String qty;
    private static String category;
    private static String id;
    private static String username;

    ObservableList<String> fragilityBoxList = FXCollections.observableArrayList("fragile?","yes","no");

    @FXML private ChoiceBox<String> fragilityField;

    @FXML private Label productName;
    @FXML private Label addResponse;

    @FXML private TextField volumeField;
    @FXML private TextField weightField;

    public static void defineParameters(String prdName, String quantity, String cat, String identifier, String user)  {
        name = prdName;
        qty = quantity;
        category = cat;
        id = identifier;
        username = user;
    }

    void storeWindow(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("storeNewPopUp.fxml"));
            Scene scene = new Scene(root);

            Stage primaryStage = new Stage();
            primaryStage.setResizable(false);
            primaryStage.setTitle("New Product");
            primaryStage.setScene(scene);
            primaryStage.initModality(Modality.WINDOW_MODAL);
            primaryStage.showAndWait();
        }catch(IOException e){
            System.out.println(e);
        }


    }

    @FXML
    void addNewProduct(){
        Main m = new Main();


        // If any text box is empty
        if (weightField.getText().isEmpty() || volumeField.getText().isEmpty() || fragilityField.getValue().isEmpty()) {
            alerts alert = new alerts();
            alert.alertBox("Error","The fields must not be empty!");
        }

        // If all text boxes are filled
        else {
            // Detect if capacity is an integer
            Boolean fragile;
            try {
                // Creates new category with text box parameters

                if(fragilityField.getValue().equalsIgnoreCase("yes")){
                    fragile = true;
                }else {
                    fragile = false;
                }
                int response = m.getEmployeeController().storeGoods(
                        name,
                        Integer.parseInt(qty),
                        Integer.parseInt(weightField.getText()),
                        Integer.parseInt(volumeField.getText()),
                        fragile,
                        category);

                if(response >= 0) {
                    m.getEmployeeController().acceptTask(
                            Integer.parseInt(id),
                            category,
                            username
                    );
                   NotificationBox.display("Success", "Product Added !");
                   addResponse.setText("Product Added!");
                }
                else if (response==-1) {
                    NotificationBox.display("Error", "Not enough space!");
                    addResponse.setText("Not enough space !");
                }

           } catch (NumberFormatException e) {
                alerts alert = new alerts();
                alert.alertBox("Error","Weight and Volume must be integers!");
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        productName.setText(" ");
        fragilityField.setValue("fragile?");
        fragilityField.setItems(fragilityBoxList);


    }

}
