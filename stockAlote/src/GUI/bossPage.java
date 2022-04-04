package GUI;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.section;

import java.net.URL;
import java.util.ResourceBundle;

public class bossPage implements Initializable {

    @FXML private TableView<section> sectionTable;

    @FXML private ComboBox<String> personalStuff;
    @FXML private TableColumn<section, String> t_categname;
    @FXML private TableColumn<section, Integer> t_categcapacity;
    @FXML private TextField newCategoryCapField;
    @FXML private TextField newCategoryField;
    @FXML private TextField removeCategoryField;
    @FXML private Label showUsername;


    // Button presses
    public void categoryButtonPressed(ActionEvent event){
        goToCategoryScene();
    }
    public void userButtonPressed(ActionEvent event){
        goToUserScene();
    }
    public void logoutButtonPressed(ActionEvent event){
        goToHomepageScene();
    }
    public void refreshButtonPressed(ActionEvent event){tableRefresh();}

    // Button pressing methods
    private void goToCategoryScene(){

        Main m = new Main();

        m.changeScene("bosspagecategory.fxml");

    }
    private void goToUserScene(){

        Main m = new Main();

        m.changeScene("bosspageuser.fxml");
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

   @FXML
   void goToPopUpADDSection(ActionEvent event){

        bossPageADDCatPopUp m = new bossPageADDCatPopUp();

        m.addNewCatWindow(event);
        tableRefresh();

    }
    @FXML
    void goToPopUpRemoveSection(ActionEvent event){
        Main m = new Main();
        m.refreshLocalFromDB();

        int result;

        TableView.TableViewSelectionModel selectionModel =  sectionTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        ObservableList<section> selectedItems = selectionModel.getSelectedItems();

        if(!selectedItems.isEmpty()){
            Boolean answer = ConfirmBox.display("Remove Section",
                    "Sure you want to remove section " + selectedItems.get(0).getCategory() + " ?");

            if(answer){
                result = m.getBossController().removeCategory(selectedItems.get(0).getCategory());

                // Set text label with the result
                if (result >= 0) {
                    NotificationBox.display("Remove Section Status",
                            "Category removed, remaining sections: " + result);

                    sectionTable.setItems(m.getWarehouseController().getSectionObList());
                }
                else if (result == -1){
                    NotificationBox.display("Remove Section Status",
                            "That section is not empty!");
                }
                else {
                    NotificationBox.display("Remove Section Status",
                            "That section does not exist!");
                }
            }
        }else{
            //Please select the section to remove
            NotificationBox.display("Remove Section Status",
                    "Please select the section to remove");
        }
    }

    // Methods of Category scene
    public void addButtonPressed(ActionEvent event){
        addNewCategory();
    }
    private void addNewCategory(){
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
                    NotificationBox.display("Success","Section Added !");
                    sectionTable.setItems(m.getWarehouseController().getSectionObList());
                }
                else if (response==-2) NotificationBox.display("Error","Not enough space !");
                else NotificationBox.display("Error","Already exists !");

            } catch (NumberFormatException e) {
                alerts alert = new alerts();
                alert.alertBox("Error","Capacity must be an integer!");
            }
        }

    }
    private void tableRefresh () {
        Main m = new Main();
        m.refreshLocalFromDB();

        sectionTable.setItems(m.getWarehouseController().getSectionObList());
    }

    public void removeButtonPressed(ActionEvent event){
        removeCategory();
    }
    private void removeCategory(){

        Main m = new Main();
        m.refreshLocalFromDB();

        int result;

        // Check if text box is empty
        if (removeCategoryField.getText().isEmpty()) {
            alerts alert = new alerts();
            alert.alertBox("Error","The fields must not be empty!");
        } else {
            result = m.getBossController().removeCategory(removeCategoryField.getText());

            // Set text label with the result
            if (result >= 0) {
                NotificationBox.display("Success","Category removed, remaining sections: " + result);
                sectionTable.setItems(m.getWarehouseController().getSectionObList());
            }
            else if (result == -1) NotificationBox.display("Error","That section is not empty!");
            else NotificationBox.display("Error","That section does not exist!");
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

    // Initialize method
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Main m = new Main();
        m.refreshLocalFromDB();

        // Fill label on top right corner with welcoming message
        showUsername.setText("Welcome "+ m.getCurrentUser().getUsername());

        personalStuff.getItems().addAll(" ","Change username","Change password");
        personalStuff.setValue(" ");


        // Get section list
        t_categname.setCellValueFactory(new PropertyValueFactory<section, String>("category") );
        t_categcapacity.setCellValueFactory(new PropertyValueFactory<section, Integer>("m3") );

        sectionTable.setItems(m.getWarehouseController().getSectionObList());

    }



}
