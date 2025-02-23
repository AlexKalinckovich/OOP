package org.example.oop.Models.ValidationClasses;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import org.example.oop.Figures.CircleFigure;
import org.example.oop.Figures.Figure;
import org.example.oop.Figures.TriangleFigure;
import org.example.oop.Models.PrintingClasses.MessageController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ParametersValidator {

    private final MessageController messageController;

    public ParametersValidator(final MessageController messageController) {
        this.messageController = messageController;
    }

    public ParametersValidator(){
        this.messageController = new MessageController();
    }

    private List<String> getFieldsParams(final TextField[] parameterFields) throws NullPointerException {
        return  Arrays.stream(parameterFields)
                .map(TextInputControl::getText)
                .map(String::trim)
                .flatMap(text -> Arrays.stream(text.split("\\s+")))
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public Optional<double[]> parseParameters(final TextField[] parameterFields){
        Optional<double[]> result = Optional.empty();
        try {
            final List<String> params = getFieldsParams(parameterFields);
            result = Optional.ofNullable(params.stream()
                    .mapToDouble(Double::parseDouble)
                    .toArray());

        }catch (NumberFormatException _){
            messageController.showAlert("Invalid enters","Values must be numeric");
        }catch (NullPointerException _) {
            messageController.showAlert("Invalid figure",
                    "Choose one of the figures");
        }
        return result;
    }

    public boolean isCorrectCoordinates(final double[] params, final Bounds bounds) {

        if(params == null || params.length == 0) return false;

        final int size = params.length;
        for (int i = 0; i < size; i++) {
            final double val = params[i];
            if (i % 2 == 0 && (val < 0 || val > bounds.getWidth())) {
                messageController.showAlert("Ошибка", "X координата выходит за пределы");
                return false;
            }
            if (i % 2 == 1 && (val < 0 || val > bounds.getHeight())) {
                messageController.showAlert("Ошибка", "X координата выходит за пределы");
                return false;
            }
        }
        return true;
    }

    public boolean isPointOutOfBounds(double x, double y, final Bounds bounds) {
        return  x <= bounds.getMinX() || x >= bounds.getMaxX() ||
                y <= bounds.getMinY() || y >= bounds.getMaxY();
    }


    public boolean isFigureInBounds(final List<Point2D> points, final Bounds bounds, final Figure figure) {
        boolean result = true;
        if (figure instanceof CircleFigure) {
            final Point2D first = points.getFirst();
            final double centerX = first.getX();
            final double centerY = first.getY();
            final double radius = points.get(1).distance(first);

            result = (centerX - radius >= bounds.getMinX()) &&
                     (centerX + radius <= bounds.getMaxX()) &&
                     (centerY - radius >= bounds.getMinY()) &&
                     (centerY + radius <= bounds.getMaxY());
        }else if(figure instanceof TriangleFigure) {

            final Point2D start = points.getFirst();
            final Point2D end = points.get(1);

            final double centerX = start.getX();
            final double centerY = start.getY();
            final double width = Math.abs(start.getX() - end.getX());
            final double height = Math.abs(start.getY() - end.getY());
            result = centerX - width / 2  >=  bounds.getMinX()  &&
                     centerX + width / 2  <=  bounds.getMaxX()  &&
                     centerY - height / 2 >= bounds.getMinY()   &&
                     centerY + height / 2 <= bounds.getMaxY();
        }

        return result;
    }
}
