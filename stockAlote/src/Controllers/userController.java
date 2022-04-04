package Controllers;

import logic.user;

public class userController {

    private user model;

    public userController(user model) {
        this.model = model;
    }

    public user getModel() {
        return model;
    }

    public void setModel(user model) {
        this.model = model;
    }

    public void changeUsername(String newUsername){
        getModel().setUsername(newUsername);
        return;
    }

    public void changePassword(String newPassword){
        getModel().setPassword(newPassword);

        return;
    }


}
