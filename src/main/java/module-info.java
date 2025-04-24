module org.example.oop {
    uses org.example.oop.Plugins.FigurePlugin;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires jdk.compiler;

    // Экспортируем пакеты
    exports org.example.oop;
    exports org.example.oop.Models.Files;
    exports org.example.oop.Plugins;
    exports org.example.oop.PluginFigures;
    exports org.example.oop.FigureInterfaces;

    // Открываем пакеты для Jackson и JavaFX
    opens org.example.oop to javafx.fxml, com.fasterxml.jackson.databind;
    opens org.example.oop.Models.Files to com.fasterxml.jackson.databind;
    opens org.example.oop.Plugins to javafx.fxml, com.fasterxml.jackson.databind;
    opens org.example.oop.PluginFigures to com.fasterxml.jackson.databind, javafx.fxml;

    provides org.example.oop.Plugins.FigurePlugin with org.example.oop.PluginFigures.StarPlugin;
}