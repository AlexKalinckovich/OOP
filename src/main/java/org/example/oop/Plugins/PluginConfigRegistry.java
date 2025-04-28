package org.example.oop.Plugins;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.oop.FiguresView.Controllers.FigureController;
import org.example.oop.Models.Files.FileManager;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class PluginConfigRegistry {
    private static final Path CONFIG_DIRECTORY = Paths.get("src/main/java/org/example/oop/PluginJars");
    private static final Path CONFIG_FILE = CONFIG_DIRECTORY.resolve("PluginConfig.json");
    private static final ObjectMapper MAPPER = (ObjectMapper) new ObjectMapper()
            .writerWithDefaultPrettyPrinter()
            .getFactory()
            .getCodec();

    /**
     * Загружает карту плагинов из JSON.
     */
    public static Map<String, String> loadPlugins() {
        try {
            if (Files.notExists(CONFIG_FILE)) {
                return new LinkedHashMap<>();
            }
            final String json = Files.readString(CONFIG_FILE);
            return MAPPER.readValue(json, new TypeReference<LinkedHashMap<String, String>>() {});
        } catch (IOException e) {
            return new LinkedHashMap<>();
        }
    }

    /**
     * Добавляет или обновляет путь для плагина с именем pluginName.
     */
    public static void addPlugin(final String pluginName, final String jarPath) {
        final Map<String, String> plugins = loadPlugins();
        if (jarPath.equals(plugins.get(pluginName))) {
            return; // уже есть запись
        }
        plugins.put(pluginName, jarPath);
        savePlugins(plugins);
    }

    /**
     * Сохраняет карту плагинов в JSON-файл.
     */
    private static void savePlugins(final Map<String, String> plugins) {
        try {
            Files.createDirectories(CONFIG_DIRECTORY);
            final String json = MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(plugins);
            Files.writeString(CONFIG_FILE, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ignored) { }
    }

    /**
     * Во время старта приложения пробегает по всем записям:
     *  - Если JAR не существует или не содержит FigurePlugin — удаляет его.
     *  - Иначе пытается загрузить плагин, чтобы проверить валидность.
     */
    public static void validateAndClean() {
        final Map<String, String> plugins = loadPlugins();
        final Iterator<Map.Entry<String, String>> it = plugins.entrySet().iterator();

        while (it.hasNext()) {
            final Map.Entry<String, String> entry = it.next();
            final String path = entry.getValue();

            final Path jar = Paths.get(path);
            boolean remove = false;

            if (Files.notExists(jar)) {
                remove = true;
            } else {
                try (final URLClassLoader loader = new URLClassLoader(
                        new URL[]{jar.toUri().toURL()},
                        PluginConfigRegistry.class.getClassLoader())
                ) {
                    final ServiceLoader<FigurePlugin> sl = ServiceLoader.load(FigurePlugin.class, loader);
                    if (!sl.iterator().hasNext()) {
                        remove = true;
                    }
                } catch (Exception e) {
                    remove = true;
                }
            }

            if (remove) {
                it.remove();
            }
        }

        // Сохраняем очищенный список
        savePlugins(plugins);
    }

    public static void initializePlugins(final FigureController figureController) {
        PluginConfigRegistry.validateAndClean();

        final Map<String, String> plugins = PluginConfigRegistry.loadPlugins();

        for (final Map.Entry<String, String> entry : plugins.entrySet()) {
            final String jarPath = entry.getValue();

            try (final URLClassLoader loader = new URLClassLoader(
                    new URL[]{Paths.get(jarPath).toUri().toURL()},
                    PluginConfigRegistry.class.getClassLoader())
            ) {
                final ServiceLoader<FigurePlugin> serviceLoader = ServiceLoader.load(FigurePlugin.class, loader);

                boolean found = false;
                for (final FigurePlugin plugin : serviceLoader) {
                    found = true;

                    System.out.println("Loaded plugin: " + plugin.getTypeName());
                    figureController.registerFigure(plugin.getTypeName(), plugin.createFigureInstance());
                    FileManager.registerPlugin(plugin);
                    FileManager.registerSubtypes(plugin.getFigureClass());
                }

                if (!found) {
                    System.out.println("Warning: No valid FigurePlugin found in " + jarPath);
                }

            } catch (Exception e) {
                System.out.println("Error loading plugin from " + jarPath + ": " + e.getMessage());
            }
        }
    }
}