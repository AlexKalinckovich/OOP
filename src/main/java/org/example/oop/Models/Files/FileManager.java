package org.example.oop.Models.Files;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import javafx.scene.Node;
import org.example.oop.Plugins.FigurePlugin;
import org.example.oop.Figures.Figure;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class FileManager {
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);
    private final JSONConverter jsonConverter = new JSONConverter();


    public void saveToFile(List<Node> nodes, Path path) throws IOException {
        final List<FigureDTO> dtos = nodes.stream()
                .map(jsonConverter::nodeToDTO)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        mapper.writeValue(path.toFile(), dtos);
    }


    public List<Node> loadFromFile(Path path) throws IOException {
        final List<FigureDTO> dtos = mapper.readValue(path.toFile(), new TypeReference<>() {});
        return dtos.stream()
                .map(jsonConverter::dtoToNode)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public void registerPlugin(final FigurePlugin plugin){
        jsonConverter.registerPlugin(plugin);
    }

    public void registerSubtypes(final Class<? extends Figure> subtype){
        mapper.registerModule(new SimpleModule(){
            @Override
            public void setupModule(SetupContext context) {
                context.registerSubtypes(subtype);
            }
        });
    }

}