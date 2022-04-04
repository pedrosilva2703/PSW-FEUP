package GUI;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class alerts {

    public void alertBox(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        Optional<ButtonType> option = alert.showAndWait();

    }
}
