package dev.csu.survivor.ui;

import com.almasb.fxgl.texture.Texture;
import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.ItemType;
import dev.csu.survivor.item.Item;
import dev.csu.survivor.world.SurvivorGameWorld;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.util.List;

public class ItemView extends BorderStackPane
{
    protected final ItemType itemType;
    protected final Item item;

    public ItemView(ItemType itemType)
    {
        super(Constants.Client.SHOP_ENTRY_WIDTH, Constants.Client.SHOP_ENTRY_HEIGHT);
        this.itemType = itemType;
        this.item = itemType.createItem();
        this.getChildren().add(createVBox());
    }

    protected VBox createVBox()
    {
        VBox box = new VBox();
        box.setPadding(new Insets(Constants.Client.SHOP_ENTRY_PADDING));
        box.setSpacing(Constants.Client.SHOP_ENTRY_INNER_SPACING);
        box.setAlignment(Pos.TOP_CENTER);

        box.getChildren().addAll(
                createItemPane(),
                createItemName(),
                createDescPane()
        );
        return box;
    }

    protected BorderStackPane createItemPane()
    {
        Texture texture = itemType.getTexture().copy();
        texture.setScaleX(Constants.Client.SHOP_ITEM_TEXTURE_SCALE);
        texture.setScaleY(Constants.Client.SHOP_ITEM_TEXTURE_SCALE);
        return new BorderStackPane(Constants.Client.SHOP_ITEM_BORDER_SIZE, Constants.Client.SHOP_ITEM_BORDER_SIZE, texture);
    }

    protected TextFlow createItemName()
    {
        Text text = new Text(itemType.getName());
        text.setFill(Color.WHITE);
        text.setFont(Font.font(Constants.Client.SHOP_ITEM_NAME_FONT));
        TextFlow itemName = new TextFlow(text);
        itemName.setTextAlignment(TextAlignment.CENTER);
        itemName.setPrefWidth(Constants.Client.SHOP_ENTRY_WIDTH);
        return itemName;
    }

    protected BorderStackPane createDescPane()
    {
        List<Text> desc = this.item.getDescription(SurvivorGameWorld.getPlayer());
        desc.forEach(text1 ->
        {
            text1.setFont(Font.font(Constants.Client.SHOP_ITEM_DESC_FONT));
        });

        VBox box = new VBox();
        box.setPadding(new Insets(20, 40, 20, 40));
        box.setSpacing(12);
        box.setAlignment(Pos.TOP_LEFT);
        box.getChildren().addAll(desc);

//        TextFlow itemDesc = new TextFlow();
//        itemDesc.getChildren().addAll(desc);
//        itemDesc.setTextAlignment(TextAlignment.LEFT);
//        itemDesc.setPrefWidth(Constants.Client.SHOP_ITEM_BORDER_SIZE - 2 * Constants.Client.SHOP_ENTRY_PADDING);
        return new BorderStackPane(Constants.Client.SHOP_ITEM_BORDER_SIZE, Constants.Client.SHOP_ITEM_BORDER_SIZE, box);
    }
}
