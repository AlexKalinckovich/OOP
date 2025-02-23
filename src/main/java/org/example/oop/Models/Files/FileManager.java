package org.example.oop.Models.Files;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.Node;
import org.example.oop.Figures.Figure;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class FileManager {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void saveToFile(final Collection<Node> figures, final Path path) throws IOException {
        mapper.writeValue(path.toFile(), figures);
    }

    public static List<Figure> loadFromFile(final Path path) throws IOException {
        return mapper.readValue(path.toFile(), new TypeReference<>() {});
    }
}