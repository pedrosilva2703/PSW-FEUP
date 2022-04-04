package Controllers;

import logic.*;

import java.util.ArrayList;

public class sectionController {

    private section model;


    public sectionController(section model) {
        this.model = model;
    }

    // Getters
    public int getID() {
        return model.getID();
    }
    public String getCategory() {
        return model.getCategory();
    }
    public int getM3() {
        return model.getM3();
    }
    public ArrayList<goods> getAllGoods(){ return model.getAllGoods(); }
    public ArrayList<task> getTaskList(){ return model.getTaskList(); }

    // Setters
    public void setID(int iD) {
        model.setID(iD);
    }
    public void setCategory(String category) {
        model.setCategory(category);
    }
    public void setM3(int m3) {
        model.setM3(m3);
    }
    public void addGood (goods newGood) { model.addGood(newGood); }
    public void addTask (task newTask) { model.addTask(newTask); }
    public void deleteTask(task t) { model.deleteTask(t); }


    // ********* SECTION METHODS *********

    // Returns true if empty | false if not empty
    public boolean sectionIsEmpty(){
        for(goods currentGood : getAllGoods()){
            if(currentGood.getQty() != 0){
                return false;
            }
        }
        return true;
    }

    // Returns Good if found | null if not found
    public goods getGood(String name) {

        for (goods currentGood: getAllGoods()) {
            if (currentGood.getName().equalsIgnoreCase(name)) {
                return currentGood;
            }
        }
        // If a good with that name does not exist
        return null;
    }

    public int removeProduct(String name, int qty, String category) {

        // Search the bunch for the right good
        for (goods currentGood : getAllGoods()) {

            // If it finds the good
            if (currentGood.getName().equals(name)) {

                // If resulting quantity is negative
                if (currentGood.getQty()-qty < 0) return -2;

                    // IF resulting quantity is positive or zero
                else {
                    currentGood.setQty(currentGood.getQty()-qty);
                    return currentGood.getQty();
                }
            }

        }
        // If the good was not found
        return -1;

    }
    public int get_availableSectionSpace(){

        int usedSpace = 0;
        for (goods good : getAllGoods()) {
            usedSpace += good.getQty()*good.getVolume_pu();
        }
         return getM3()- usedSpace;

    }



}
