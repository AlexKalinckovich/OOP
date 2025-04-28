package org.example.oop.FiguresView.Controllers;


import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.example.oop.Models.Files.FileManager;
import org.example.oop.Models.PrintingClasses.MessageController;

import javafx.application.Platform;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

import javafx.scene.Node;
import org.example.oop.Plugins.FigurePlugin;
import org.example.oop.Plugins.PluginConfigRegistry;

public class FileController {

    public static void handleSave(List<Node> nodes) {
        final FileChooser fileChooser = new FileChooser();
        Path projectDir = Paths.get(System.getProperty("user.dir"));
        fileChooser.setInitialDirectory(projectDir.toFile());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        final File file = fileChooser.showSaveDialog(null);
        final FileManager fileManager = new FileManager();

        if (file != null) {
            try {
                fileManager.saveToFile(nodes, file.toPath());
            } catch (IOException e) {
                Platform.runLater(() -> MessageController.showAlert("File error", "Failed to save file"));
            }
        }
    }

    public static void handleLoad(Pane drawingArea) {
        final FileChooser fileChooser = new FileChooser();
        Path projectDir = Paths.get(System.getProperty("user.dir"));
        fileChooser.setInitialDirectory(projectDir.toFile());
        final File file = fileChooser.showOpenDialog(null);
        final FileManager fileManager = new FileManager();


        if (file != null) {
            try {
                List<Node> result = fileManager.loadFromFile(file.toPath());
                drawingArea.getChildren().addAll(result);
            } catch (IOException e) {
                Platform.runLater(() -> MessageController.showAlert("File error", "Failed to load file"));
            }
        }
    }

    public static Optional<FigurePlugin> handleJarPluginLoad() {
        final FileChooser chooser = new FileChooser();
        chooser.setTitle("Выберите JAR-файл плагина");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Java Archive", "*.jar")
        );
        final File jarFile = chooser.showOpenDialog(null);
        if (jarFile == null) {
            return Optional.empty();
        }

        try {
            final URL jarUrl = jarFile.toURI().toURL();
            final URLClassLoader loader = new URLClassLoader(
                    new URL[]{jarUrl},
                    FigurePlugin.class.getClassLoader()
            );

            final ServiceLoader<FigurePlugin> sl = ServiceLoader.load(FigurePlugin.class, loader);
            final Iterator<FigurePlugin> it = sl.iterator();
            if (!it.hasNext()) {
                MessageController.showAlert("Error","В выбранном JAR нет реализации FigurePlugin.");
                return Optional.empty();
            }

            final FigurePlugin plugin = it.next();
            if (plugin.getTypeName() == null || plugin.getTypeName().isBlank()) {
                MessageController.showAlert("Error","Неверный плагин: отсутствует имя фигуры.");
                return Optional.empty();
            }

            PluginConfigRegistry.addPlugin(plugin.getTypeName(),jarFile.getAbsolutePath());

            return Optional.of(plugin);
        } catch (IOException e) {
            MessageController.showAlert("Error","Ошибка при загрузке JAR: " + e.getMessage());
            return Optional.empty();
        }
    }

}
