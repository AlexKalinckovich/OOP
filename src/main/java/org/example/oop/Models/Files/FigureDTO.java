package org.example.oop.Models.Files;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record FigureDTO(
        @JsonProperty("type") String type,
        @JsonProperty("parameters") double[] parameters,
        @JsonProperty("fillColor") String fillColor,
        @JsonProperty("strokeColor") String strokeColor,
        @JsonProperty("strokeWidth") double strokeWidth,
        @JsonProperty("dashPattern") List<Double> dashPattern
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
