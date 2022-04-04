package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class managerNewTaskPopUp implements Initializable {
    private static Stage stg;
    private static boolean typeChoosen = true;
    private static boolean storeChoosen = false;
    private static boolean moveChoosen = false;
    private static boolean shipoutChoosen = false;

    @FXML
    private ChoiceBox<String> typeField;
    ObservableList<String> fragilityBoxList = FXCollections.observableArrayList("Task Type", "Store", "Move", "Ship out");

    @FXML
    private TextField destinationCategoryField;
    @FXML
    private TextField originCategoryField;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField qtyField;


     void window(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("newTaskPopUp.fxml"));
            Scene scene = new Scene(root);

            Stage primaryStage = new Stage();
            stg = primaryStage;
            primaryStage.setResizable(false);
            primaryStage.setTitle("New Task");
            primaryStage.setScene(scene);
            primaryStage.initModality(Modality.WINDOW_MODAL);
            primaryStage.showAndWait();
        }catch(IOException e){
            System.out.println(e);
        }
    }

    @FXML
    void taskType(){

        typeField.getSelectionModel().selectedItemProperty().addListener((v, oldvalue, newvalue) -> {

            try {
                Parent pane;
                if ("Store".equals(newvalue)) {
                    storeChoosen = true;
                    pane = FXMLLoader.load(getClass().getResource("storeTaskPopUp.fxml"));
                    stg.getScene().setRoot(pane);

                } else if ("Move".equals(newvalue)) {
                    moveChoosen = true;
                    pane = FXMLLoader.load(getClass().getResource("moveTaskPopUp.fxml"));
                    stg.getScene().setRoot(pane);

                } else if ("Ship out".equals(newvalue)) {
                    shipoutChoosen = true;
                    pane = FXMLLoader.load(getClass().getResource("shipoutTaskPopUp.fxml"));
                    stg.getScene().setRoot(pane);

                } else if ("Task Type".equalsIgnoreCase(newvalue)) {
                    typeChoosen = true;
                    pane = FXMLLoader.load(getClass().getResource("newTaskPopUp.fxml"));
                    stg.getScene().setRoot(pane);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @FXML
    void createTask() {
        Main m = new Main();
        // If any text box is empty
        if (productNameField.getText().isEmpty() || originCategoryField.getText().isEmpty()
                || (qtyField.getText().isEmpty() &&  !typeField.getValue().equalsIgnoreCase("Move") ) ){
            alerts alert = new alerts();
            alert.alertBox("Error", "The fields must not be empty!");
        }

        // If all text boxes are filled
        else {
            // Detect if capacity is an integer
            try {
                int response = Integer.MIN_VALUE;

                // Creates new category with text box parameters
                if (typeField.getValue().equalsIgnoreCase("Store") ||
                        typeField.getValue().equalsIgnoreCase("Ship out")) {
                    response = m.getManagerController().createTask(typeField.getValue(),
                            productNameField.getText(),
                            originCategoryField.getText(),
                            "---",
                            Integer.parseInt(qtyField.getText()));
                } else {
                    // IF task is MOVE
                    if (destinationCategoryField.getText().isEmpty()) {
                        alerts alert = new alerts();
                        alert.alertBox("Error", "The fields must not be empty!");
                    } else {
                        response =m.getWarehouseController().getSectionID(destinationCategoryField.getText());
                        if(response != -1){
                            response = m.getManagerController().createTask(typeField.getValue(),
                                    productNameField.getText(),
                                    originCategoryField.getText(),
                                    destinationCategoryField.getText(),
                                    0
                            );
                        }

                    }

                }

                if (response >= 0)
                    NotificationBox.display("Success", "Task Added !");
                else if (response == -2)
                    NotificationBox.display("Error", "Section does not exist !");
                else if(response == -1)
                    NotificationBox.display("Error", "Destination does not exist !");

            } catch (NumberFormatException e) {
                alerts alert = new alerts();
                alert.alertBox("Error", "Quantity must be an integer!");
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (typeChoosen) {
            typeField.setValue("Select Type");
            typeChoosen = false;
        } else if (storeChoosen) {
            typeField.setValue("Store");
            storeChoosen = false;
        } else if (moveChoosen) {
            typeField.setValue("Move");
            moveChoosen = false;
        } else if (shipoutChoosen) {
            typeField.setValue("Ship out");
            shipoutChoosen = false;
        }

        typeField.setItems(fragilityBoxList);

    }


}
