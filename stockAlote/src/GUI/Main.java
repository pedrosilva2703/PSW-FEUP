package GUI;

import Controllers.bossController;
import Controllers.employeeController;
import Controllers.managerController;
import Controllers.warehouseController;
import db.SQL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.*;

import logic.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

//To run this code with IntelliJ,
//go to Run > Edit configurations > Modify options > Add VM options
//and in the new VM option prompt that appears paste the following command:
// --module-path "javafx-sdk-17.0.1\lib" --add-modules javafx.controls,javafx.fxml
//then press apply and ok, you're ready to run!

public class Main extends Application {

    // Stage
    private static Stage stg;

    // Database
    private static boolean dbIsEmpty;
    private static SQL db;

    // Warehouse
    private static warehouse WH;

    private static warehouseController warehouseController;

    // Boss

    private static boss babyboss;
    private static bossController bossController;
    private static user currentUser;

    // Manager

    private static manager currentManager;
    private static managerController managerController;

    // Employee

    private static employee currentEmployee;
    private static employeeController employeeController;


    // Getters
    public boolean isDbEmpty() {
        return dbIsEmpty;
    }
    public SQL getDb(){return db;}
    public Controllers.warehouseController getWarehouseController() {
        return warehouseController;
    }
    public user getCurrentUser() {
        return currentUser;
    }
    public Controllers.bossController getBossController() {
        return bossController;
    }


    public manager getCurrentManager() {
        return currentManager;
    }
    public Controllers.managerController getManagerController() {
        return managerController;
    }


    public employee getCurrentEmployee() {
        return currentEmployee;
    }
    public Controllers.employeeController getEmployeeController() {
        return employeeController;
    }

    // Setters
    public void setDbEmpty(boolean dbIsEmpty) {
        Main.dbIsEmpty = dbIsEmpty;
    }
    public void setCurrentUser(user currentUser) {
        Main.currentUser = currentUser;
    }

    // Graphical User Interface
    @Override
    public void start(Stage primaryStage){
        try{
            stg = primaryStage;
            primaryStage.setResizable(false);
            Parent root = FXMLLoader.load(getClass().getResource("homepage.fxml"));
            primaryStage.setTitle("StockAlote Warehouse Software");
            primaryStage.setOnCloseRequest( e -> {
                e.consume();
                closeProgram();
            } );

            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        }catch(Exception e){
            System.out.println(e);
        }

    }
    public void changeScene(String fxml){
        try{
            Parent pane = FXMLLoader.load(getClass().getResource(fxml));
            stg.getScene().setRoot(pane);
        }catch(Exception e){
            System.out.println(e);
        }
    }
    private void closeProgram () {
        Boolean answer = ConfirmBox.display ("Exit", "Sure you want to leave ?");
        if(answer)
            stg.close();
    }

    //Update from DB function
    public static void updateLocalFromDB() {
        //Retrieve managers
        try {
            WH = new warehouse();
            warehouseController = new warehouseController(WH, db);

            // Create boss
            babyboss = new boss();
            bossController = new bossController(babyboss, WH, db);

            // Define WH parameters
            bossController.FromDBnewWareHouse();

            //Retrieve other classes

            //Retrieve boss
            db.hasRows("boss");
            warehouseController.FromDBaddUser2WH(db.getUserFields("boss"));

            if (db.hasRows("manager")) {
                //Retrieve first row
                warehouseController.FromDBaddUser2WH(db.getUserFields("manager"));
                //While has more rows, retrieve
                while (db.getResultSet().next()) {
                    warehouseController.FromDBaddUser2WH(db.getUserFields("manager"));
                }
            }

            //Retrieve employees
            if (db.hasRows("employee")) {
                //Retrieve first row
                warehouseController.FromDBaddUser2WH(db.getUserFields("employee"));
                //While has more rows, retrieve
                while (db.getResultSet().next()) {
                    warehouseController.FromDBaddUser2WH(db.getUserFields("employee"));
                }
            }

            //Retrieve sections
            if (db.hasRows("section")) {

                //Retrieve first row
                List<Object> FromDbNewSection = db.getSectionFields();
                bossController.FromDBnewCategory(FromDbNewSection.get(0).toString(), Integer.parseInt(FromDbNewSection.get(1).toString()));
                //While has more rows, retrieve
                while (db.getResultSet().next()) {
                    FromDbNewSection = db.getSectionFields();
                    bossController.FromDBnewCategory(FromDbNewSection.get(0).toString(), Integer.parseInt(FromDbNewSection.get(1).toString()));
                }
            }

            //Retrieve goods
            if (db.hasRows("goods")) {
                //Auxiliary MVC employee to store goods
                employee aux_Employee = new employee();
                employeeController aux_employeeController = new employeeController(aux_Employee, bossController.getWH(), db);

                //Retrieve first row
                List<Object> FDBnewGood = db.getGoodsFields();
                aux_employeeController.FromDBstoreGoods(FDBnewGood);

                //While has more rows, retrieve
                while (db.getResultSet().next()) {
                    FDBnewGood = db.getGoodsFields();
                    aux_employeeController.FromDBstoreGoods(FDBnewGood);
                }
            }

            //Retrieve task
            if (db.hasRows("task")) {
                manager aux_Manager = new manager();
                managerController aux_managerController = new managerController(aux_Manager, bossController.getWH(), db);

                //Retrieve first row
                List<Object> FDBnewTask = db.getTaskFields();
                aux_managerController.FromDBcreateTask(FDBnewTask);

                //While has more rows, retrieve
                while (db.getResultSet().next()) {
                    FDBnewTask = db.getTaskFields();
                    aux_managerController.FromDBcreateTask(FDBnewTask);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshLocalFromDB() {
        //Retrieve managers
        try {

            WH.clearWH();

            warehouseController = new warehouseController(WH, db);

            // Create boss
            babyboss = new boss();
            bossController = new bossController(babyboss, WH, db);

            // Define WH parameters
            bossController.FromDBnewWareHouse();

            //Retrieve other classes

            //Retrieve boss
            db.hasRows("boss");
            warehouseController.FromDBaddUser2WH(db.getUserFields("boss"));

            if (db.hasRows("manager")) {
                //Retrieve first row
                warehouseController.FromDBaddUser2WH(db.getUserFields("manager"));
                //While has more rows, retrieve
                while (db.getResultSet().next()) {
                    warehouseController.FromDBaddUser2WH(db.getUserFields("manager"));
                }
            }

            //Retrieve employees
            if (db.hasRows("employee")) {
                //Retrieve first row
                warehouseController.FromDBaddUser2WH(db.getUserFields("employee"));
                //While has more rows, retrieve
                while (db.getResultSet().next()) {
                    warehouseController.FromDBaddUser2WH(db.getUserFields("employee"));
                }
            }

            //Retrieve sections
            if (db.hasRows("section")) {

                //Retrieve first row
                List<Object> FromDbNewSection = db.getSectionFields();
                bossController.FromDBnewCategory(FromDbNewSection.get(0).toString(), Integer.parseInt(FromDbNewSection.get(1).toString()));
                //While has more rows, retrieve
                while (db.getResultSet().next()) {
                    FromDbNewSection = db.getSectionFields();
                    bossController.FromDBnewCategory(FromDbNewSection.get(0).toString(), Integer.parseInt(FromDbNewSection.get(1).toString()));
                }
            }

            //Retrieve goods
            if (db.hasRows("goods")) {
                //Auxiliary MVC employee to store goods
                currentEmployee = new employee();
                employeeController = new employeeController(currentEmployee, WH, db);

                //Retrieve first row
                List<Object> FDBnewGood = db.getGoodsFields();
                employeeController.FromDBstoreGoods(FDBnewGood);

                //While has more rows, retrieve
                while (db.getResultSet().next()) {
                    FDBnewGood = db.getGoodsFields();
                    employeeController.FromDBstoreGoods(FDBnewGood);
                }
            }

            //Retrieve task
            if (db.hasRows("task")) {
                currentManager = new manager();
                managerController = new managerController(currentManager,WH, db);

                //Retrieve first row
                List<Object> FDBnewTask = db.getTaskFields();
                managerController.FromDBcreateTask(FDBnewTask);

                //While has more rows, retrieve
                while (db.getResultSet().next()) {
                    FDBnewTask = db.getTaskFields();
                    managerController.FromDBcreateTask(FDBnewTask);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Main Method
    public static void main(String[] args){

        db = new SQL();

        //This comment is just for testing fresh new db
        //db.dropSQLtables();
        //db.createSQLtables();

        WH = new warehouse();
        warehouseController = new warehouseController(WH, db);


        dbIsEmpty = !(db.hasRows("boss"));

        System.out.println("Already has info on DB: " + db.hasRows("boss"));


        //If Database has content
        if (db.hasRows("boss")) {
            updateLocalFromDB();
        }
        // If Database is empty
        else {

            // Create boss
            babyboss = new boss();
            bossController = new bossController(babyboss, WH, db);

        }

        currentManager = new manager();
        managerController = new managerController(currentManager,bossController.getWH(), db);

        currentEmployee = new employee();
        employeeController = new employeeController(currentEmployee,bossController.getWH(), db);

        launch(args);
    }
}
