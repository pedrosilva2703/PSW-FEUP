package Controllers;


import cli.encrypter;
import db.SQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class warehouseController {

    private warehouse model;

    private SQL db;

    public warehouseController(warehouse model, SQL db) {
        this.model = model;
        this.db = db;
    }

    public String getName() {
        return model.getName();
    }

    public String getLocation() {
        return model.getLocation();
    }

    public int getM3() {
        return model.getM3();
    }

    public ArrayList<section> getAllSections() {
        return model.getAllSections();
    }

    public ArrayList<user> getAllUsers() {
        return model.getAllUsers();
    }

    // Setters
    public void setName(String name) {
        model.setName(name);
    }

    public void setLocation(String location) {
        model.setLocation(location);
    }

    public void setM3(int m3) {
        model.setM3(m3);
    }

    public void addSection(section newSection) {
        model.addSection(newSection);
    }

    public void addUser(user newUser) {
        model.addUser(newUser);
    }


    // ************** OTHER CONTROLLERS **************** //
    protected sectionController readOnlySection(section currentSection) {

        sectionController sectionController = new sectionController(currentSection);
        return sectionController;

    }

    // ************** WAREHOUSE METHODS **************** //

    public int getWHfreeSpace() {

        int usedSpace = 0;
        for (section currentSection : getAllSections()) {
            usedSpace += currentSection.getM3();
        }
        return getM3() - usedSpace;

    }

    // Returns number of sections
    public int getSectionCount() {
        return getAllSections().size();
    }

    // Returns section id if it exists | -1 if section does not exist
    public int getSectionID(String category) {
        for (section currentSection : getAllSections()) {

            if (currentSection.getCategory().equalsIgnoreCase(category)) {

                return currentSection.getID();
            }
        }
        // If a section with that name does not exist
        return -1;
    }

    public int removeSection(String category) {

        int currentID = getSectionID(category);

        getAllSections().remove(currentID);

        for (int i = currentID; i < getAllSections().size(); i++) {
            int newID = getAllSections().get(i).getID() - 1;
            getAllSections().get(i).setID(newID);
        }
        return getSectionCount();
    }


    public section getSection(int i) {
        return getAllSections().get(i);
    }

    public List<String> getSectionNamesForFilter() {

        List<String> returnList = new ArrayList<>();

        returnList.add("All sections");

        for (section currentSection : getAllSections()) {
            returnList.add(currentSection.getCategory());
        }
        return returnList;

    }

    public ObservableList<Map<String, Object>> getTasksObList(String filter) {

        ObservableList<Map<String, Object>> items =
                FXCollections.<Map<String, Object>>observableArrayList();

        for (section currentSection : getAllSections()) {
            if (filter.equalsIgnoreCase("All sections") || filter.equalsIgnoreCase(currentSection.getCategory())) {
                for (task currentTask : currentSection.getTaskList()) {

                    Map<String, Object> itemString = new HashMap<>();
                    itemString.put("id", Integer.toString(currentTask.getId()));
                    itemString.put("type", currentTask.getTaskType());
                    itemString.put("product", currentTask.getProductName());
                    itemString.put("origin", currentTask.getProductCategory());
                    itemString.put("destination", currentTask.getDestinationCategory());
                    if( currentTask.getTaskType().equalsIgnoreCase("Move") ){
                        itemString.put("qty", "All");
                    }
                    else{
                        itemString.put("qty", Integer.toString(currentTask.getProductQty()));
                    }
                    itemString.put("doneby", currentTask.getDoneBy());
                    itemString.put("start", currentTask.getStartTime());
                    itemString.put("end", currentTask.getEndTime());
                    if (currentTask.getEndTime().equalsIgnoreCase("---"))
                        itemString.put("complete", "false");
                    else
                        itemString.put("complete", "true");

                    items.add(itemString);
                }
            }

        }

        return items;
    }

    public ObservableList<Map<String, Object>> getReportObList(String filter) {

        ObservableList<Map<String, Object>> items =
                FXCollections.<Map<String, Object>>observableArrayList();

        for (section currentSection : getAllSections()) {
            if (filter.equalsIgnoreCase("All sections") || filter.equalsIgnoreCase(currentSection.getCategory())) {
                for (task currentTask : currentSection.getTaskList()) {
                    if (currentTask.isCompleted()) {
                        Map<String, Object> itemString = new HashMap<>();
                        itemString.put("id", Integer.toString(currentTask.getId()));
                        itemString.put("type", currentTask.getTaskType());
                        itemString.put("product", currentTask.getProductName());
                        itemString.put("origin", currentTask.getProductCategory());
                        itemString.put("destination", currentTask.getDestinationCategory());
                        if( currentTask.getTaskType().equalsIgnoreCase("Move") ){
                            itemString.put("qty", "All");
                        }
                        else{
                            itemString.put("qty", Integer.toString(currentTask.getProductQty()));
                        }
                        itemString.put("doneby", currentTask.getDoneBy());
                        itemString.put("start", currentTask.getStartTime());
                        itemString.put("end", currentTask.getEndTime());

                        if (currentTask.getEndTime().equalsIgnoreCase("---"))
                            itemString.put("complete", "false");
                        else
                            itemString.put("complete", "true");

                        items.add(itemString);
                    }
                }
            }

        }

        return items;


    }

    public ObservableList<Map<String, Object>> getInventoryObList(String filter) {

        ObservableList<Map<String, Object>> items =
                FXCollections.<Map<String, Object>>observableArrayList();

        for (section currentSection : getAllSections()) {

            if (filter.equalsIgnoreCase("All sections") || filter.equalsIgnoreCase(currentSection.getCategory())) {
                for (goods currentGoods : currentSection.getAllGoods()) {
                    Map<String, Object> itemString = new HashMap<>();

                    itemString.put("category", currentSection.getCategory());
                    itemString.put("product", currentGoods.getName());
                    itemString.put("quantity", Integer.toString(currentGoods.getQty()));
                    itemString.put("weight", Integer.toString(currentGoods.getWeight_pu()));
                    itemString.put("volume", Integer.toString(currentGoods.getVolume_pu()));
                    itemString.put("fragility", Boolean.toString(currentGoods.getFragility()));

                    items.add(itemString);
                }
            }
        }
        return items;
    }

    public ObservableList<user> getUserObList() {
        ObservableList<user> userList = FXCollections.observableArrayList();
        userList.addAll(getAllUsers());
        return userList;
    }

    public ObservableList<section> getSectionObList() {
        ObservableList<section> sectionList = FXCollections.observableArrayList();
        sectionList.addAll(getAllSections());
        return sectionList;
    }

    public void addUser2WH(List<Object> user){

        user newUser = new user();
        encrypter hash = new encrypter();
        newUser.setName(user.get(0).toString());
        newUser.setContact(user.get(1).toString());
        newUser.setBirthdate(user.get(2).toString());
        newUser.setUsername(user.get(3).toString());
        newUser.setPassword(hash.generateStrongPasswordHash(user.get(4).toString()));
        //newUser.setPassword(user.get(4).toString());
        newUser.setRole(user.get(5).toString());

        db.insertUser(newUser.getName(), newUser.getBirthdate(), newUser.getContact(), newUser.getUsername(), newUser.getPassword(), getName(), newUser.getRole());

        addUser(newUser);
    }

    public void removeUserFromWh(String username, int index){
        getAllUsers().remove(index);
        db.deleteUser(username);
    }

    public void FromDBaddUser2WH(List<Object> user) {
        user newUser = new user();
        newUser.setName(user.get(0).toString());
        newUser.setContact(user.get(1).toString());
        newUser.setBirthdate(user.get(2).toString());
        newUser.setUsername(user.get(3).toString());
        newUser.setPassword(user.get(4).toString());
        newUser.setRole(user.get(5).toString());

        addUser(newUser);
    }

    public void updateUsernameWHandDB (String oldUsername, String newUsername){

        for(user currentUser: getAllUsers() ){
            if(currentUser.getUsername().equals(oldUsername)){

                currentUser.setUsername(newUsername);

                db.updateUsername(newUsername,oldUsername,currentUser.getRole());
            }
        }

    }
    public void updatePasswordWHandDB (user currentUser, String newPassword){

        for(user myUser: getAllUsers() ){
            if(currentUser.getUsername().equals(myUser.getUsername())){

                myUser.setPassword(newPassword);

                db.updatePassword(currentUser.getUsername(),newPassword,currentUser.getRole());
            }
        }

    }

    public user checkLogin(List<Object> credentials){

        for (user currentUser : getAllUsers()) {
            if (currentUser.getUsername().equals(credentials.get(0).toString())) {
                encrypter hash = new encrypter();
                if (hash.validatePassword(credentials.get(1).toString(), currentUser.getPassword()))
                    return currentUser;
            }
        }

        return null;
    }

    public boolean checkUsername(String username) {

        for (user currentUser : getAllUsers()) {
            if (currentUser.getUsername().equals(username))
                return true;
        }
        return false;
    }

    public boolean checkPassword(String pw){

        for (user currentUser : getAllUsers()) {
            encrypter hash = new encrypter();
            if (hash.validatePassword(pw, currentUser.getPassword()))
                return true;
        }
        return false;
    }

}
