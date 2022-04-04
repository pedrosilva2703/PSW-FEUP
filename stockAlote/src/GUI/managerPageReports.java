package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class managerPageReports implements Initializable {

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
    void logoutButtonPressed(ActionEvent event){
        goToHomepageScene();
    }
    @FXML
    void taskButtonPressed(ActionEvent event){
        goToTasksScene();
    }
    @FXML
    void refreshButtonPressed(ActionEvent event){
        tableRefresh();
        Main m = new Main();
        m.changeScene("managerpagereports.fxml");
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

    @FXML
    void createPDFreport(ActionEvent event){

        String userHomeFolder = System.getProperty("user.home");
        String path=userHomeFolder+"/Desktop";
        Timestamp data = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdate = new SimpleDateFormat("HH.mm_ddMMyyyy");

        File textFile = new File(path, sectionFilter.getValue()+"_on_"+sdate.format(data)+".txt");

        Main m = new Main();
        ObservableList<Map<String, Object>> tasksList = m.getWarehouseController().getReportObList(sectionFilter.getValue());


        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(textFile));
            sdate = new SimpleDateFormat("HH:mm dd-MM-yyyy");
            out.write("Report of "+sectionFilter.getValue()+" generated in "+sdate.format(data) +" by "+m.getCurrentUser().getUsername());
            out.newLine();
            out.write(" +---------------------------------------------------------------------------------------------------------------------------------------------------+");
            out.newLine();
            out.write(String.format("%5s %10s %15s %20s %20s %5s %20s %18s %18s %10s ",
                                    " |id|","type |","product |","origin |","destination |","qty |","done by |","start |","end |","complete?|"));
            out.newLine();
            out.write(" +---------------------------------------------------------------------------------------------------------------------------------------------------+");
            out.newLine();
            for(Map<String, Object> currentTask: tasksList ){
                out.write(String.format("%5s %10s %15s %20s %20s %5s %20s %16s %16s %10s ",
                                "|"+currentTask.get("id").toString()+" |",
                                currentTask.get("type").toString()+" |",
                                currentTask.get("product").toString()+" |",
                                currentTask.get("origin").toString()+" |",
                                currentTask.get("destination").toString()+" |",
                                currentTask.get("qty").toString()+" |",
                                currentTask.get("doneby").toString()+" |",
                                currentTask.get("start").toString()+ " |",
                                currentTask.get("end").toString()+" |",
                                currentTask.get("complete").toString()+"|"));
                out.newLine();
            }
            out.write(" +---------------------------------------------------------------------------------------------------------------------------------------------------+");
            out.newLine();


            out.close();
            NotificationBox.display("Success","File created on Desktop!");

        }catch(IOException e){
            System.out.println(e);
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


    // New scenes
    void goToInventoryScene(){
        Main m = new Main();
        m.changeScene("managerpageinventory.fxml");
    }
    void goToHomepageScene(){
        Main m = new Main();
        m.changeScene("homepage.fxml");
    }
    void goToTasksScene(){
        Main m = new Main();
        m.changeScene("managerpagetasks.fxml");
    }

    @FXML
    void filterDefined(ActionEvent event) {
        tableRefresh();
    }

    protected void tableRefresh() {
        Main m = new Main();
        m.refreshLocalFromDB();

        ObservableList<Map<String, Object>> tasksList = m.getWarehouseController().getReportObList(sectionFilter.getValue());

        tasksTable.setItems(tasksList);
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
