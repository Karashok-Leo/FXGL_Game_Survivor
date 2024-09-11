package dev.csu.survivor.ui;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * 自带一个白色圆角矩形边框的 StackPane
 */
public class BorderStackPane extends StackPane
{
    public BorderStackPane(double width, double height, Node... nodes)
    {
        super();
        this.getChildren().add(createBorder(width, height));
        this.getChildren().addAll(nodes);
    }

    /**
     * 创建一个白色圆角矩形边框
     *
     * @param width  边框宽度
     * @param height 边框高度
     * @return 边框的 Rectangle 对象
     */
    public static Rectangle createBorder(double width, double height)
    {
        Rectangle border = new Rectangle(width, height, null);
        border.setStroke(Color.WHITE);
        border.setStrokeWidth(4.0);
        border.setArcWidth(25.0);
        border.setArcHeight(25.0);
        return border;
    }
}
