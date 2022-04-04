package logic;

import java.util.ArrayList;

public class section {

    // Atributes
    private int id;
    //private String name;
    private String category;
    private int m3;
    private ArrayList<goods> bunchOfGoods  = new ArrayList<>();
    private ArrayList<task> taskList = new ArrayList<>();

    // Getters
    public int getID() {
        return id;
    }
    /*public String getName() {
        return name;
    }*/
    public String getCategory() {
        return category;
    }
    public int getM3() {
        return m3;
    }
    public ArrayList<goods> getAllGoods(){ return bunchOfGoods; }
    public ArrayList<task> getTaskList(){ return taskList; }


    // Setters
    public void setID(int id) {
        this.id = id;
    }
    /*public void setName(String name) {
        this.name = name;
    }*/
    public void setCategory(String category) {
        this.category = category;
    }
    public void setM3(int m3) {
        this.m3 = m3;
    }
    public void addGood (goods newGood) { bunchOfGoods.add(bunchOfGoods.size(),newGood); }
    public void addTask (task newTask) { taskList.add(newTask); }
    public void deleteTask (task t) { taskList.remove(t); }


}
