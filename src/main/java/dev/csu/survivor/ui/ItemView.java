package dev.csu.survivor.ui;

import com.almasb.fxgl.texture.Texture;
import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.ItemType;
import dev.csu.survivor.item.Item;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

public class ItemView extends StackPane
{
    protected final ItemType itemType;
    protected final Item item;

    public ItemView(ItemType itemType)
    {
        super();
        this.itemType = itemType;
        this.item = itemType.createItem();
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
        VBox box = new VBox();
        box.setPadding(new Insets(Constants.Client.SHOP_ENTRY_PADDING));
        box.setSpacing(Constants.Client.SHOP_ENTRY_INNER_SPACING);
        box.setAlignment(Pos.TOP_CENTER);

        StackPane itemPane = createItemPane();

        Text text = new Text(itemType.getItemName());
        text.setFill(Color.WHITE);
        text.setFont(Font.font(Constants.Client.HUD_FONT));

        List<Text> tooltip = this.item.getTooltip();

        box.getChildren().addAll(itemPane, text);
        box.getChildren().addAll(tooltip);

        return box;
    }

    protected StackPane createItemPane()
    {
        Texture texture = itemType.getItemTexture();
        texture.setScaleX(Constants.Client.SHOP_ITEM_TEXTURE_SCALE);
        texture.setScaleY(Constants.Client.SHOP_ITEM_TEXTURE_SCALE);
        Rectangle itemBorder = createBorder(Constants.Client.SHOP_ITEM_BORDER_SIZE, Constants.Client.SHOP_ITEM_BORDER_SIZE);
        return new StackPane(itemBorder, texture);
    }
}
