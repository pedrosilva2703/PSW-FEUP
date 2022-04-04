package Controllers;

import db.SQL;
import logic.*;

import java.util.List;

public class managerController{

    private manager model;
    private warehouse WH;
    private SQL db;

    public managerController (manager model,warehouse WH, SQL db){
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

    // ************ OTHER CONTROLLERS  ***************
    protected warehouseController readOnlyWarehouse(){
        warehouseController warehouseController = new warehouseController(WH,db);
        return  warehouseController;
    }
    protected sectionController readOnlySection(section currentSection){
        sectionController sectionController = new sectionController(currentSection);
        return  sectionController;
    }


    // ********* MANAGER METHODS *********
    // Returns free warehouse space
    public int get_availableWarehouseSpace(){


        return readOnlyWarehouse().getWHfreeSpace();

    }

    // Returns free space of the section | -1 if section does not exist
    public int get_availableSectionSpace(String category){

        int sectionID = readOnlyWarehouse().getSectionID(category);
        if (sectionID < 0) return sectionID;
        else return readOnlySection(readOnlyWarehouse().getSection(sectionID)).get_availableSectionSpace();

    }


    public int createTask(String type, String pname, String pcategory, String destination, int pqty){

        int sectionID = readOnlyWarehouse().getSectionID(pcategory);
        if(sectionID==-1) return -2;
        section currentSection = readOnlyWarehouse().getSection(sectionID);
        sectionController sectionController =  readOnlySection(currentSection);

        // If category exists
        if (sectionID >=0 ) {
            task newTask = new task();

            if(type.equalsIgnoreCase("discard")) {

                newTask.setId(sectionController.getTaskList().size());
                newTask.setTaskType(type);
                newTask.setProductName(pname);
                newTask.setProductCategory(pcategory);
                newTask.setDestinationCategory("none");
                newTask.setProductQty(pqty);
                newTask.setCompleted(true);
                newTask.setStartTime("n/a");
                newTask.setEndTime("n/a");
                newTask.setDoneBy("---");
                sectionController.addTask(newTask);

            }
            else{
                // Creates task

                newTask.setId(sectionController.getTaskList().size());
                newTask.setTaskType(type);
                newTask.setProductName(pname);
                newTask.setProductCategory(pcategory);
                newTask.setDestinationCategory(destination);
                newTask.setProductQty(pqty);
                newTask.setCompleted(false);
                newTask.setStartTime("---");
                newTask.setEndTime("---");
                newTask.setDoneBy("---");
                sectionController.addTask(newTask);

            }

            db.insertTask(newTask.getId(), type, pname, pcategory, destination, pqty,
                    newTask.isCompleted(), newTask.getStartTime(), newTask.getEndTime(), newTask.getDoneBy());

            // Returns task id
            return newTask.getId();
        }

        // If category does not exist
        return -1;

    }

    // returns 0 if task deleted successfully | -1 if id is negative | -2 if section does not exist | -3 if task in progress
    public int deleteTask(int id, String category){
        if (id < 0 || category.isEmpty()) return -1;

        int sectionID = readOnlyWarehouse().getSectionID(category);
        if (sectionID < 0) return -2;
        section currentSection = readOnlyWarehouse().getSection(sectionID);

        String startTime = currentSection.getTaskList().get(id).getStartTime();

        if (startTime.equalsIgnoreCase("---") || currentSection.getTaskList().get(id).isCompleted()) {
            task task = new task();
            for (task currentTask : currentSection.getTaskList()) {
                if (currentTask.getId() == id) task = currentTask;
            }
            currentSection.deleteTask(task);
        }
        else return -3;


        db.removeTask(id, category);

        // Update id's of other tasks
        for (task currentTask : currentSection.getTaskList()) {
            int taskID = currentTask.getId();

            if (taskID > id) {
                currentTask.setId(taskID-1);
                db.updateTaskId(taskID, category);
            }
        }
        return 0;
    }

    public int FromDBcreateTask(List<Object> task) {
        int sectionID = readOnlyWarehouse().getSectionID(task.get(9).toString() );
        section currentSection = readOnlyWarehouse().getSection(sectionID);
        sectionController sectionController =  readOnlySection(currentSection);

        // Creates task
        task newTask = new task();
        newTask.setId( Integer.parseInt(task.get(0).toString()) );
        newTask.setTaskType(task.get(1).toString() );
        newTask.setProductName(task.get(2).toString());
        newTask.setProductCategory(task.get(3).toString());
        newTask.setDestinationCategory(task.get(4).toString() );
        newTask.setProductQty( Integer.parseInt(task.get(5).toString()) );
        newTask.setCompleted( Boolean.parseBoolean(task.get(6).toString()) );
        newTask.setStartTime( task.get(7).toString() );
        newTask.setEndTime( task.get(8).toString() );
        newTask.setDoneBy(task.get(10).toString() );

        sectionController.addTask(newTask);

        // Returns task id
        return newTask.getId();

    }


}
