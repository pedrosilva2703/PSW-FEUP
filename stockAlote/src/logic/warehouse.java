package logic;

import java.util.ArrayList;

public class warehouse {

    private String name;
    private String location;
    private int m3;
    private ArrayList<section> sections = new ArrayList<>();
    private ArrayList<user> users = new ArrayList<>();

    // Getters
    public String getName() {
        return name;
    }
    public String getLocation() {
        return location;
    }
    public int getM3() {
        return m3;
    }
    public ArrayList<section> getAllSections() {
        return sections;
    }
    public ArrayList<user> getAllUsers() { return users;}

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setM3(int m3) {
        this.m3 = m3;
    }
    public void addSection ( section newSection){ sections.add(sections.size(),newSection);}
    public void addUser ( user newUser) { users.add(users.size(),newUser);}

    public void clearWH(){
        sections.clear();
        users.clear();
    }


}

