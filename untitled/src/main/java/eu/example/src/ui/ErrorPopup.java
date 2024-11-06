package eu.example.src.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ErrorPopup {

    public static void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("A apărut o eroare!");
        alert.setContentText(message);

        alert.showAndWait();
    }
}
