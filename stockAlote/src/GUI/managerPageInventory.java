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

public class managerPageInventory implements Initializable {

    ObservableList<String> sectionFilterList;

    @FXML private ComboBox<String> personalStuff;
    @FXML private TableView inventoryTable;
    @FXML private TableColumn<Map,String> t_category;
    @FXML private TableColumn<Map,String> t_pname;
    @FXML private TableColumn<Map,String> t_fragility;
    @FXML private TableColumn<Map,String> t_qty;
    @FXML private TableColumn<Map,String> t_volume;
    @FXML private TableColumn<Map,String> t_weight;
    @FXML private ComboBox<String> sectionFilter;
    @FXML private Label showUsername;

    // Button presses
    public void reportsButtonPressed (ActionEvent event){
        goToReportsScene();
    }
    public void tasksButtonPressed (ActionEvent event){
        goToTasksScene();
    }
    public void logoutButtonPressed(ActionEvent event){
        goToHomepageScene();
    }
    @FXML
    void refreshButtonPressed(ActionEvent event){
        updateInventoryTable();
        Main m = new Main();
        m.changeScene("managerpageinventory.fxml");
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

    @FXML
    void sectionButtonPressed(ActionEvent event) {

        if(sectionFilter.getValue().equalsIgnoreCase("All sections")){
            NotificationBox.display("Error","Please select a specific section");
        }
        else{
            checkSectionSpace(sectionFilter.getValue());
        }

    }
    @FXML
    void warehouseButtonPressed(ActionEvent event) {
        NotificationBox.display("Warehouse Free Space", "Available Free Space in Warehouse: "+ checkWhSpace());

    }

    // New scenes
    void goToReportsScene(){
        Main m = new Main();
        m.changeScene("managerpagereports.fxml");
    }
    void goToTasksScene(){
        Main m = new Main();
        m.changeScene("managerpagetasks.fxml");
    }
    void goToHomepageScene(){
        Main m = new Main();
        m.changeScene("homepage.fxml");
    }

    // Methods
    private void checkSectionSpace(String section) {
        Main m = new Main();


        int result = m.getManagerController().get_availableSectionSpace(section);

        if (result >= 0){
            NotificationBox.display("Section Available Space",  section + " has "+ result + " m3 available");
        }
        else {
            NotificationBox.display("Error", "The Section " + section + " don't exist!");
        }
    }

    private int checkWhSpace() {
        Main m = new Main();


        return m.getManagerController().get_availableWarehouseSpace();
    }

    @FXML
    void filterDefined(ActionEvent event) {
        updateInventoryTable();
    }

    private void updateInventoryTable(){
        Main m = new Main();
        m.refreshLocalFromDB();

        t_category.setCellValueFactory(new MapValueFactory<>("category"));
        t_pname.setCellValueFactory(new MapValueFactory<>("product"));
        t_qty.setCellValueFactory(new MapValueFactory<>("quantity"));
        t_weight.setCellValueFactory(new MapValueFactory<>("weight"));
        t_volume.setCellValueFactory(new MapValueFactory<>("volume"));
        t_fragility.setCellValueFactory(new MapValueFactory<>("fragility"));

        ObservableList<Map<String, Object>> items = m.getWarehouseController().getInventoryObList( sectionFilter.getValue() );

        inventoryTable.setItems(items);
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

        updateInventoryTable();

    }

}
