package logic;

public class user {

    // Attributes
    private String name;

    private String role;
    private String contact;
    private String birthdate;  // format dd-mm-yyyy
    private String username;
    private String password;


    // Getters
    public String getName() {
        return name;
    }
    public String getContact() {
        return contact;
    }
    public String getBirthdate() {
        return birthdate;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(String role) {
        this.role = role;
    }
}

