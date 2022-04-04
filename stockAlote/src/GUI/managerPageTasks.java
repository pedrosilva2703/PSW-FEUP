package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class managerPageTasks implements Initializable {

    ObservableList<String> sectionFilterList;

    @FXML private ComboBox<String> sectionFilter;

    @FXML private ComboBox<String> personalStuff;
    @FXML private Label showUsername;

    @FXML private Button logoutButton;

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


    // Button presses
    @FXML
    void inventoryButtonPressed (ActionEvent event){
        goToInventoryScene();
    }
    @FXML
    void reportsButtonPressed (ActionEvent event){
        goToReportsScene();
    }
    @FXML
    void logoutButtonPressed(ActionEvent event){
        goToHomepageScene();
    }
    @FXML
    void createButtonPressed(ActionEvent event){
        goToNewTask(event);
    }
    @FXML
    void removeButtonPressed(ActionEvent event){
        deleteTask();
    }
    @FXML
    void refreshButtonPressed(ActionEvent event){
        tableRefresh();
        Main m = new Main();
        m.changeScene("managerpagetasks.fxml");
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

    private void deleteTask(){

        TableView.TableViewSelectionModel selectionModel =  tasksTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Map<String, Object>> items = selectionModel.getSelectedItems();

        Main m = new Main();


        int result = m.getManagerController().deleteTask(Integer.parseInt(items.get(0).get("id").toString()), items.get(0).get("origin").toString());

        if (result >= 0)
            NotificationBox.display("Success", "Task removed !");

        else if (result == -1)
            NotificationBox.display("Error", "Task id is negative!");
        else if (result == -2)
            NotificationBox.display("Error", "Section does not exist!");
        else if (result == -3)
            NotificationBox.display("Error", "Task is in progress!");

        tableRefresh();
    }


    // New scenes
    void goToInventoryScene(){
        Main m = new Main();
        m.changeScene("managerpageinventory.fxml");
    }
    void goToReportsScene(){
        Main m = new Main();
        m.changeScene("managerpagereports.fxml");
    }
    void goToHomepageScene(){
        Main m = new Main();
        m.changeScene("homepage.fxml");
    }
    void goToNewTask(ActionEvent event){
        managerNewTaskPopUp popUp = new managerNewTaskPopUp();
        popUp.window(event);

        tableRefresh();

    }

    @FXML
    void filterDefined(ActionEvent event) {
        tableRefresh();
    }

    public void tableRefresh() {
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
