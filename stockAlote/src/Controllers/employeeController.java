package Controllers;

import db.SQL;
import logic.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class employeeController{

    private employee model;
    private warehouse WH;
    private SQL db;

    public employeeController (employee model,warehouse WH, SQL db){
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


    // ********* OTHER CONTROLLERS *********
    protected warehouseController readOnlyWarehouse(){
        warehouseController warehouseController = new warehouseController(WH,db);
        return warehouseController;

    }
    protected sectionController readOnlySection(section currentSection){
        sectionController sectionController = new sectionController(currentSection);
        return  sectionController;
    }

    // ********* EMPLOYEE METHODS *********
    public int checkSectionANDGood (String category, String name){

        int sectionID = readOnlyWarehouse().getSectionID(category);
        if(sectionID==-1)
            return -2;
        else{
            section currentSection = readOnlyWarehouse().getSection(sectionID);
            sectionController sectionController =  readOnlySection(currentSection);
            if (sectionController.getGood(name) == null)
                return -1;
            else
                return 1;
        }
    }
    // Returns product quantity if success | -1 if no space in section | -2 if section does not exist
    public int storeGoods(String name, int qty, int weight_pu, int volume_pu, boolean fragile, String category){

        int sectionID = readOnlyWarehouse().getSectionID(category);
        if(sectionID==-1) return -2;

        section currentSection = readOnlyWarehouse().getSection(sectionID);
        sectionController sectionController =  readOnlySection(currentSection);
        // If category already exists
        if (sectionID != -1) {

            //If section doesn't have enough space for this qty
            if( sectionController.get_availableSectionSpace() < (qty*volume_pu) ) return -1;


            // If good already exists
            if (sectionController.getGood(name) != null) {
                int old_quantity =sectionController.getGood(name).getQty();
                sectionController.getGood(name).setQty(old_quantity+qty);

                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!_DB FUNCTION_!!!!!!!!!!//
                db.updateGoodQTY(name,category,old_quantity+qty);
            }

            // If good does not exist
            else addProduct(name, qty ,weight_pu, volume_pu, fragile,currentSection);

        }

        // If category does not yet exist
        else return -2;

        // Returns quantity of product added/updated
        return sectionController.getGood(name).getQty();

    }
    public int FromDBstoreGoods(List<Object> goods){
        //Retrieve variables from arraylist
        String name = goods.get(0).toString();
        String category = goods.get(1).toString();
        int qty = Integer.parseInt(goods.get(2).toString());
        int weight_pu = Integer.parseInt(goods.get(3).toString());
        int volume_pu = Integer.parseInt(goods.get(4).toString());
        boolean fragile =Boolean.parseBoolean(goods.get(5).toString());

        int sectionID = readOnlyWarehouse().getSectionID(category);
        section currentSection = readOnlyWarehouse().getSection(sectionID);
        sectionController sectionController =  readOnlySection(currentSection);

        //Add new good
        goods newGood = new goods();

        newGood.setID(sectionController.getAllGoods().size());
        newGood.setName(name);
        newGood.setQty(qty);
        newGood.setWeight_pu(weight_pu);
        newGood.setVolume_pu(volume_pu);
        newGood.setFragility(fragile);

        sectionController.addGood(newGood);

        // Returns quantity of product added/updated
        return qty;

    }

    public void addProduct(String name, int qty, int weight_pu, int volume_pu, boolean fragility, section currentSection ){

        sectionController sectionController =  readOnlySection(currentSection);
        goods newGood = new goods();

        newGood.setID(sectionController.getAllGoods().size());
        newGood.setName(name);
        newGood.setQty(qty);
        newGood.setWeight_pu(weight_pu);
        newGood.setVolume_pu(volume_pu);
        newGood.setFragility(fragility);

        sectionController.addGood(newGood);

        db.insertGood(newGood.getID(), name, qty, weight_pu, volume_pu, fragility, sectionController.getCategory() );

    }

    //Na GUI, esta funçao só vai poder ser chamada como intuito de cumprir uma task criada pelo manager
    // Returns quantity of goods if success | -1 if Good was not found | -2 if resulting quantity is negative
    public int shipOutGoods(String name, int qty, String category){

        int sectionID = readOnlyWarehouse().getSectionID(category);
        if(sectionID<0) return -1;
        section currentSection = readOnlyWarehouse().getSection(sectionID);

        int currentQTY = readOnlySection(currentSection).removeProduct(name,qty,category);
        if(currentQTY>=0){  //se nao ocorreu erro

            db.updateGoodQTY(name, category, currentQTY);
        }
        return currentQTY;
    }


    //Na GUI, esta funçao vai poder ser utilizada quando o employee achar necessário
    // Returns 0 if success | -1 if Good was not found | -2 if resulting quantity is negative
    public int discardDamagedProduct(String name, int qty, String category){

        return shipOutGoods(name,qty,category);
    }


    // Returns 0 if success | -1 if Good was not found | -2 if resulting quantity is negative |
    // | -3 if there is not enough space in section | -4 if section does not exist
    public int moveGood(String name, String oldCategory, String newCategory){

        int oldSectionID = readOnlyWarehouse().getSectionID(oldCategory);
        int newSectionID = readOnlyWarehouse().getSectionID(newCategory);

        section oldSection = readOnlyWarehouse().getSection(oldSectionID);
        section newSection = readOnlyWarehouse().getSection(newSectionID);

        goods currentGood = readOnlySection(oldSection).getGood(name);
        if (currentGood == null) {
            System.out.println("RETURN -1");
            return -1;
        }

        int result = storeGoods(name, currentGood.getQty(), currentGood.getWeight_pu(),
                currentGood.getVolume_pu(), currentGood.getFragility(), newCategory);


        // if good was added successfully
        if (result >= 0) {

            // Returns 0 if success | -1 if Good was not found | -2 if resulting quantity is negative
            int currentQTY = readOnlySection(oldSection).removeProduct(name, currentGood.getQty(), oldCategory);
            if(currentQTY>=0){  //se nao ocorreu erro

                db.updateGoodQTY(name, oldCategory, currentQTY);
            }
            return currentQTY;
        }

        // if there is not enough space in section (-3) | if section does not exist (-4)
        else return result-2;
    }


    public int acceptTask(int taskId, String category, String doneBy){
        int sectionID = readOnlyWarehouse().getSectionID(category);
        ArrayList<task> taskArray = readOnlyWarehouse().getSection(sectionID).getTaskList();

        // If sections does not exist
        if (sectionID < 0) return -1;

        // If no tasks are available
        if (taskArray.size() == 0) return -2;

        for (task currentTask : taskArray) {
            // Found the right task
            if (currentTask.getId() == taskId) {
                Timestamp data = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat sdate = new SimpleDateFormat("HH:mm dd-MM-yyyy");
                currentTask.setStartTime( sdate.format(data) );
                currentTask.setDoneBy(doneBy);

                db.updateTaskStart(taskId, category, currentTask.getStartTime(),doneBy );
            }
        }
        return 0;
    }


    public int ackTask(int taskId, String category){
        int sectionID = readOnlyWarehouse().getSectionID(category);
        ArrayList<task> taskArray = readOnlyWarehouse().getSection(sectionID).getTaskList();

        // If sections does not exist
        if (sectionID < 0) return -1;

        // If no tasks are available
        if (taskArray.size() == 0) return -2;

        for (task currentTask : taskArray) {
            // Found the right task
            if (currentTask.getId() == taskId) {
                Timestamp data = new Timestamp(System.currentTimeMillis());
                SimpleDateFormat sdate = new SimpleDateFormat("HH:mm dd-MM-yyyy");
                currentTask.setEndTime( sdate.format(data) );
                currentTask.setCompleted(true);

                db.updateTaskEnd(taskId, category, currentTask.getEndTime());

            }
        }
        return 0;
    }

}
