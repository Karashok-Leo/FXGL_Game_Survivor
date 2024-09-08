package dev.csu.survivor.ui;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.Nullable;

public class BorderStackPane extends StackPane
{
    public static Rectangle createBorder(double width, double height)
    {
        Rectangle border = new Rectangle(width, height, null);
        border.setStroke(Color.WHITE);
        border.setStrokeWidth(4.0);
        border.setArcWidth(25.0);
        border.setArcHeight(25.0);
        return border;
    }

    public BorderStackPane(double width, double height)
    {
        this(width, height, null);
    }

    public BorderStackPane(double width, double height, @Nullable Node node)
    {
        super();
        this.getChildren().add(createBorder(width, height));
        if (node != null)
            this.getChildren().add(node);
    }
}
