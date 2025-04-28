module org.example.oop {
    uses org.example.oop.Plugins.FigurePlugin;

    // Зависимости JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires jdk.compiler;

    // Экспорт API-пакетов
    exports org.example.oop;                          // общий API
    exports org.example.oop.Models.Files;             // работа с файлами
    exports org.example.oop.Plugins;                  // интерфейс FigurePlugin
    exports org.example.oop.FigureInterfaces;         // MouseDrawable и др.
    exports org.example.oop.Figures;                  // класс Figure и подклассы
    exports org.example.oop.FiguresView.Controllers;

    opens org.example.oop to javafx.fxml, com.fasterxml.jackson.databind;
    opens org.example.oop.Models.Files to com.fasterxml.jackson.databind;
    opens org.example.oop.Plugins to javafx.fxml, com.fasterxml.jackson.databind;
}
