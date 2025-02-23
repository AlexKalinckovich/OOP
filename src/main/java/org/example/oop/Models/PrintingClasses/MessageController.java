package org.example.oop.Models.PrintingClasses;

import javafx.scene.control.Alert;

public class MessageController {

    public void showAlert(String title, String message) {
        /*
         * Alert - стандартное диалоговое окно
         * AlertType.ERROR - тип окна (иконка ошибки)
         */
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
