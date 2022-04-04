package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class employeePageTasks implements Initializable {

    ObservableList<String> tasksFilterList;

    @FXML private Label showUsername;
    @FXML private Button logoutButton;

    ObservableList<String> sectionFilterList;
    @FXML private ComboBox<String> sectionFilter;

    @FXML private ComboBox<String> personalStuff;

    @FXML private TableView tasksTable;
    @FXML private TableColumn<Map, String> idColumn;
    @FXML private TableColumn<Map, String> typeColumn;
    @FXML private TableColumn<Map, String> productColumn;
    @FXML private TableColumn<Map, String> originColumn;
    @FXML private TableColumn<Map, String> destinationColumn;
    @FXML private TableColumn<Map, String> qtyColumn;
    @FXML private TableColumn<Map, String> donebyColumn;
    @FXML private TableColumn<Map, String> startColumn;
    @FXML private TableColumn<Map, String> endColumn;
    @FXML private TableColumn<Map, String> completeColumn;

    @FXML private ToggleGroup taskType;

    private String stringTaskType;

    // Button presses
    @FXML
    void logoutButtonPressed(ActionEvent event) {
        goToHomepageScene();
    }

    @FXML
    void startTaskButtonPressed(ActionEvent event){
        goToStartTask(event);
    }

    @FXML
    void finishTaskButtonPressed(ActionEvent event){
        goToFinishTask();
    }
    @FXML
    void refreshButtonPressed(ActionEvent event){
        tableRefresh();
        Main m = new Main();
        m.changeScene("employeepagetasks.fxml");
    }
    @FXML void discardButtonPressed(ActionEvent event){
        goToDiscardProduct(event);
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

    // New Scenes
    private void goToStartTask(ActionEvent event){

        Main m = new Main();


        int result;
        int response;

        TableView.TableViewSelectionModel selectionModel = tasksTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Map<String, Object>> selectedItems = selectionModel.getSelectedItems();

        if (!selectedItems.isEmpty()) {

            if (!selectedItems.get(0).get("start").toString().equalsIgnoreCase("---")) {
                NotificationBox.display("Error", "That task already started");
                return;
            }

            Boolean answer = ConfirmBox.display("Start Task",
                    "Sure you want to start tasks nº " + selectedItems.get(0).get("id") + " ?");
            if (answer) {
                switch (selectedItems.get(0).get("type").toString()) {
                    case "Store":

                        result = m.getEmployeeController().checkSectionANDGood(
                                selectedItems.get(0).get("origin").toString(),
                                selectedItems.get(0).get("product").toString()
                        );

                        if (result == -2) {
                            NotificationBox.display("Section not found", " Please inform manager that "
                                    + selectedItems.get(0).get("origin").toString() + " does not exist");
                        } else if (result == -1) {
                            // adiciona novo produto
                            employeeStoreNewPopUp w = new employeeStoreNewPopUp();
                            w.defineParameters(
                                    selectedItems.get(0).get("product").toString(),
                                    selectedItems.get(0).get("qty").toString(),
                                    selectedItems.get(0).get("origin").toString(),
                                    selectedItems.get(0).get("id").toString(),
                                    m.getCurrentUser().getName()
                            );

                            w.storeWindow(event);
                            tableRefresh();

                        } else {

                            // atualiza quantidade
                             response = m.getEmployeeController().storeGoods(
                                    selectedItems.get(0).get("product").toString(),
                                    Integer.parseInt(selectedItems.get(0).get("qty").toString()),
                                    1, 1, true,
                                    selectedItems.get(0).get("origin").toString()
                            );
                            if (response >= 0) {
                                NotificationBox.display("Success", "Product Added !");
                                m.getEmployeeController().acceptTask(
                                        Integer.parseInt(selectedItems.get(0).get("id").toString()),
                                        selectedItems.get(0).get("origin").toString(),
                                        m.getCurrentUser().getName()
                                );

                            } else if (response == -1) {
                                NotificationBox.display("Error", "Not enough space!");
                            }

                            tableRefresh();

                        }
                        break;
                    case "Move":
                        // Returns 0 if success | -1 if Good was not found | -2 if resulting quantity is negative |
                        // | -3 if there is not enough space in section | -4 if section does not exist

                        response = m.getEmployeeController().moveGood(
                                selectedItems.get(0).get("product").toString(),
                                selectedItems.get(0).get("origin").toString(),
                                selectedItems.get(0).get("destination").toString()
                        );
                        switch (response) {
                            case -1:
                                NotificationBox.display("Error", "Good not found !");
                                break;
                            case -2:
                                NotificationBox.display("Error", "Not enough quantity in origin !");
                                break;
                            case -3:
                                NotificationBox.display("Error", "Not enough space on destination !");
                                break;
                            case -4:
                                NotificationBox.display("Error","Destination not exists !");
                                break;
                            case 0:
                                NotificationBox.display("Success", "Product Moved !");
                                m.getEmployeeController().acceptTask(
                                        Integer.parseInt(selectedItems.get(0).get("id").toString()),
                                        selectedItems.get(0).get("origin").toString(),
                                        m.getCurrentUser().getName()
                                );
                                break;
                            default:
                                break;
                        }
                         tableRefresh();
                        break;
                    case "Ship out":
                        // Returns quantity of goods if success | -1 if Good was not found | -2 if resulting quantity is negative

                        response = m.getEmployeeController().shipOutGoods(
                                selectedItems.get(0).get("product").toString(),
                                Integer.parseInt(selectedItems.get(0).get("qty").toString()),
                                selectedItems.get(0).get("origin").toString()
                        );
                        switch (response) {
                            case -1:
                                NotificationBox.display("Error", "Good not found !");
                                break;
                            case -2:
                                NotificationBox.display("Error", "Not enough quantity in origin !");
                                break;
                            default:
                                NotificationBox.display("Success", "Product Shipped out !");
                                m.getEmployeeController().acceptTask(
                                        Integer.parseInt(selectedItems.get(0).get("id").toString()),
                                        selectedItems.get(0).get("origin").toString(),
                                        m.getCurrentUser().getName()
                                );
                                break;
                        }
                        tableRefresh();
                        break;
                    case "discard":
                        NotificationBox.display("Error","Impossible to start that particular task !");
                    default:
                        break;

                }

            }

        } else {
            //Please select the section to start
            NotificationBox.display("Start Task Status",
                    "Please select the task to start");
        }

    }

    private void goToFinishTask(){
        // only who started can finish
        // only started tasks can be finished
        //finished tasks cant be finished

        Main m = new Main();


        TableView.TableViewSelectionModel selectionModel = tasksTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Map<String, Object>> selectedItems = selectionModel.getSelectedItems();

        // some row is selected
        if (!selectedItems.isEmpty()) {

            if (selectedItems.get(0).get("complete").toString().equalsIgnoreCase("true")) {
                NotificationBox.display("Error", "That task is already complete");
                return;
            }

            //the start was started
            if (!selectedItems.get(0).get("start").toString().equalsIgnoreCase("---")) {
                // only who started can finish
                if (selectedItems.get(0).get("doneby").toString().equalsIgnoreCase(m.getCurrentUser().getName())) {

                    int result = m.getEmployeeController().ackTask(
                            Integer.parseInt(selectedItems.get(0).get("id").toString()),
                            selectedItems.get(0).get("origin").toString());
                    if (result == -1) {
                        NotificationBox.display("Error", "Contact Manager, section doesn't exist!");
                    } else if (result == -2) {
                        NotificationBox.display("Error", "No tasks available !");
                    } else {
                        NotificationBox.display("Success", "Task Finished!");
                    }
                } else {
                    NotificationBox.display("Error", "Only who started can finish!");

                }

            } else {
                NotificationBox.display("Error", "Impossible to finish without start!");
            }


        } else {
            //Please select the section to remove
            NotificationBox.display("Finish Task Status",
                    "Please select the task to finish");
        }

        tableRefresh();
    }

    private void goToDiscardProduct(ActionEvent event) {

        employeeDiscardPopUp w = new employeeDiscardPopUp();
        w.discardWindow(event);

        tableRefresh();

    }


    private void goToHomepageScene(){
        Main m = new Main();
        m.changeScene("homepage.fxml");
    }

    @FXML
    void filterDefined(ActionEvent event) {
        tableRefresh();
    }

    private void tableRefresh() {
        Main m = new Main();
        m.refreshLocalFromDB();
        tasksTable.setItems(m.getWarehouseController().getTasksObList(sectionFilter.getValue()));

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Main m = new Main();


        showUsername.setText("Welcome " + m.getCurrentUser().getUsername());
        personalStuff.getItems().addAll(" ","Change username","Change password");
        personalStuff.setValue(" ");



        // Get section list
        List<String> sectionList = m.getWarehouseController().getSectionNamesForFilter();
        sectionFilterList = FXCollections.observableArrayList(sectionList);
        sectionFilter.setItems(sectionFilterList);
        sectionFilter.setValue("All sections");

        idColumn.setCellValueFactory(new MapValueFactory<>("id"));
        typeColumn.setCellValueFactory(new MapValueFactory<>("type"));
        productColumn.setCellValueFactory(new MapValueFactory<>("product"));
        originColumn.setCellValueFactory(new MapValueFactory<>("origin"));
        destinationColumn.setCellValueFactory(new MapValueFactory<>("destination"));
        qtyColumn.setCellValueFactory(new MapValueFactory<>("qty"));
        donebyColumn.setCellValueFactory(new MapValueFactory<>("doneby"));
        startColumn.setCellValueFactory(new MapValueFactory<>("start"));
        endColumn.setCellValueFactory(new MapValueFactory<>("end"));
        completeColumn.setCellValueFactory(new MapValueFactory<>("complete"));

        tableRefresh();


    }

}
