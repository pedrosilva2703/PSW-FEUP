package Controllers;

import db.SQL;
import logic.*;

import java.util.List;

public class bossController{

    private boss model;
    private warehouse WH;
    private SQL db;

    public bossController (boss model, warehouse WH, SQL db){
        this.model = model;
        this.WH = WH;
        this.db = db;
    }

    public String getName() {
        return model.getName();
    }
    public String getContact() {
        return model.getContact();
    }
    public String getBirthdate() {
        return model.getBirthdate();
    }
    public String getUsername() {
        return model.getUsername();
    }
    public String getPassword() {
        return model.getPassword();
    }
    public warehouse getWH() {
        return WH;
    }
    public String getRole() { return model.getRole();}
    public SQL getDB() { return db;}

    // Setters
    public void setName(String name) {
        model.setName(name);
    }
    public void setContact(String contact) {
        model.setContact(contact);
    }
    public void setBirthdate(String birthdate) {
        model.setBirthdate(birthdate);
    }
    public void setUsername(String username) {
        model.setUsername(username);
    }
    public void setPassword(String password) {
        model.setPassword(password);
    }
    public void setRole (String role) { model.setRole(role);}


    // ************ OTHER CONTROLLERS  ***************
    protected warehouseController readOnlyWarehouse(){

       warehouseController warehouseController = new warehouseController(WH,db);
       return  warehouseController;
    }


    protected sectionController readOnlySection(section currentSection){
        sectionController sectionController = new sectionController(currentSection);
        return  sectionController;
    }


    // ************** BOSS METHODS **************** //
    public void newWareHouse(List<Object> newWH){

        readOnlyWarehouse().setName(newWH.get(0).toString());
        readOnlyWarehouse().setLocation(newWH.get(1).toString());
        readOnlyWarehouse().setM3(Integer.parseInt(newWH.get(2).toString()));


        db.insertWH(readOnlyWarehouse().getName(), readOnlyWarehouse().getLocation(), readOnlyWarehouse().getM3());
    }
    public void FromDBnewWareHouse(){
        List<Object> newWH =   db.getWHFields();
        readOnlyWarehouse().setName(newWH.get(0).toString());
        readOnlyWarehouse().setLocation(newWH.get(1).toString());
        readOnlyWarehouse().setM3(Integer.parseInt(newWH.get(2).toString()));
    }

    public int addUserFromGUI(List<Object> info){

       for (user currentuser: readOnlyWarehouse().getAllUsers()){
           if(currentuser.getUsername().equalsIgnoreCase(info.get(3).toString())){
               // user already exists in the warehouse
               return -1;
           }
       }
       // If is new to warehouse
       readOnlyWarehouse().addUser2WH(info);
       return 1;
        
    }

    // Returns 0 if user removed | -1 if user does not exist
    public int removeUserFromGUI (String username){
        int index = 0;

        for (user currentUser: readOnlyWarehouse().getAllUsers()){
            if(currentUser.getUsername().equalsIgnoreCase(username)){
                // user exists in the warehouse
                index = readOnlyWarehouse().getAllUsers().indexOf(currentUser);
                if (currentUser.getRole().equalsIgnoreCase("boss"))
                    return -2;
                readOnlyWarehouse().removeUserFromWh(username, index);
                return 0;
            }
        }
        // If user does not exist
        return -1;

    }

    // Returns number of sections | -1 if category already exists | -2 if not enough space
    public int newCategory(String category/*, String name*/, int m3){

        // If something was found
        if (readOnlyWarehouse().getSectionID(category) != -1) return -1;

        else if (readOnlyWarehouse().getWHfreeSpace() < m3) return -2;

            // if category does not yet exist, adds a new one
        else{
            section newSection = new section();
            newSection.setCategory(category);
            newSection.setM3(m3);
            newSection.setID(readOnlyWarehouse().getAllSections().size());
            readOnlyWarehouse().addSection(newSection);


            db.insertSection(newSection.getCategory(), newSection.getM3(), readOnlyWarehouse().getName());

            return readOnlyWarehouse().getAllSections().size(); // Adds new section with a new category, returns index
        }

    }
    public int FromDBnewCategory(String category, int m3){
        section newSection = new section();
        newSection.setCategory(category);
        newSection.setM3(m3);
        newSection.setID(readOnlyWarehouse().getAllSections().size());
        readOnlyWarehouse().addSection(newSection);

        return readOnlyWarehouse().getAllSections().size(); // Adds new section with a new category, returns index

    }

    // Returns number of sections | -1 if section is not empty | -2 if section does not exist
    public int removeCategory(String category){

        int sectionID = readOnlyWarehouse().getSectionID(category);

        //if section exists
        if (sectionID != -1) {

            section currentSection = readOnlyWarehouse().getAllSections().get(sectionID);
            //if empty, remove
            if( readOnlySection(currentSection).sectionIsEmpty() ){

                db.deleteSection(category);

                return readOnlyWarehouse().removeSection(category);
            }

            //if not empty, return -1
            else return -1;
        }

        //if section does not exist
        else return -2;
    }


}
