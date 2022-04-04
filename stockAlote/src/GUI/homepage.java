package GUI;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class homepage {



    public homepage() {
    }

    @FXML
    void enterPressed (KeyEvent event){

        if(event.getCode () == KeyCode.ENTER){
            goToLogInScreen();
        }
    }

    public void buttonClicked(ActionEvent event){
        goToLogInScreen();
    }

    private void goToLogInScreen(){
        Main m = new Main();

        if(m.isDbEmpty()==false) {
            m.refreshLocalFromDB();
            m.changeScene("login.fxml");
        }
        else if(m.isDbEmpty()==true) {
            m.changeScene("firstsetup.fxml");
        }

    }


}