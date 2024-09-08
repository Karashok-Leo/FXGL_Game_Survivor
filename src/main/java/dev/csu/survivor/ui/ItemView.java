package dev.csu.survivor.ui;

import com.almasb.fxgl.texture.Texture;
import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.ItemType;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ItemView extends StackPane
{
    protected final ItemType itemType;

    public ItemView(ItemType itemType)
    {
        super();
        this.itemType = itemType;
        this.getChildren().addAll(createBorder(200, 480), createVBox());
    }

    protected Rectangle createBorder(double width, double height)
    {
        Rectangle border = new Rectangle(width, height, null);
        border.setStroke(Color.WHITE);
        border.setStrokeWidth(4.0);
        border.setArcWidth(25.0);
        border.setArcHeight(25.0);
        return border;
    }

    protected VBox createVBox()
    {
        VBox box = new VBox(Constants.Client.HUD_SPACING);
        box.setAlignment(Pos.TOP_CENTER);
        StackPane itemPane = createItemPane();
        itemPane.setTranslateY(20);
        box.getChildren().addAll(itemPane);
        return box;
    }

    protected StackPane createItemPane()
    {
        Texture texture = itemType.getItemTexture();
        texture.setScaleX(3.2);
        texture.setScaleY(3.2);

        return new StackPane(createBorder(160, 160), texture);
    }
}
