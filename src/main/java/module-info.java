module org.example.oop {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;
    requires annotations;
    requires jdk.compiler;

    // Экспортируем пакеты
    exports org.example.oop;
    exports org.example.oop.Models.Files;
    exports org.example.oop.Plugins;
    exports org.example.oop.PluginFigures;

    // Открываем пакеты для Jackson и JavaFX
    opens org.example.oop to javafx.fxml, com.fasterxml.jackson.databind;
    opens org.example.oop.Models.Files to com.fasterxml.jackson.databind;
    opens org.example.oop.Plugins to javafx.fxml, com.fasterxml.jackson.databind;
    opens org.example.oop.PluginFigures to com.fasterxml.jackson.databind, javafx.fxml;

    // Указываем, что модуль использует FigurePlugin
    uses org.example.oop.Plugins.FigurePlugin;
}