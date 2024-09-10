package dev.csu.survivor.ui;

import com.almasb.fxgl.texture.Texture;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.InventoryComponent;
import dev.csu.survivor.ui.menu.ShopMenu;
import dev.csu.survivor.world.SurvivorGameWorld;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class InventoryPane extends HBox
{
    private static final int ITEMS_PER_PAGE = Constants.Client.INVENTORY_COLS * Constants.Client.INVENTORY_ROWS;

    private int currentPage;
    protected List<BorderStackPane> itemPanes;
    protected GridPane grid;
    protected VBox selectItemBox;

    public InventoryPane()
    {
        super();

        this.grid = new GridPane();
        this.grid.setAlignment(Pos.CENTER);
        this.grid.setHgap(Constants.Client.SHOP_ENTRY_OUTER_SPACING);
        this.grid.setVgap(Constants.Client.SHOP_ENTRY_OUTER_SPACING);
        this.updateInventory();
        this.setPage(0);
        HBox buttonBox = this.createButtonBox();

        VBox inventoryBox = new VBox(grid, buttonBox);
        inventoryBox.setSpacing(Constants.Client.SHOP_ENTRY_OUTER_SPACING);

        this.selectItemBox = new VBox();
        selectItemBox.setSpacing(Constants.Client.SHOP_ENTRY_OUTER_SPACING);

        HBox wholeBox = new HBox(inventoryBox, selectItemBox);
        wholeBox.setAlignment(Pos.CENTER);
        wholeBox.setSpacing(Constants.Client.SHOP_ENTRY_OUTER_SPACING);

        this.getChildren().addAll(wholeBox);
    }

    public void updateInventory()
    {
        this.itemPanes = SurvivorGameWorld.getPlayer()
                .getComponent(InventoryComponent.class)
                .getInventory()
                .stream()
                .map(this::createInventoryEntry)
                .toList();
    }

    protected HBox createButtonBox()
    {
        BorderStackPane previous = ShopMenu.createBorderButton(
                "Previous",
                200,
                button -> this.prev()
        );
        BorderStackPane next = ShopMenu.createBorderButton(
                "Next",
                200,
                button -> this.next()
        );
        HBox buttonBox = new HBox(previous, next);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(80);
        return buttonBox;
    }

    protected BorderStackPane createInventoryEntry(InventoryComponent.ItemEntry itemEntry)
    {
        Texture texture = itemEntry.type().getTexture().copy();
        texture.setScaleX(Constants.Client.INVENTORY_ITEM_TEXTURE_SCALE);
        texture.setScaleY(Constants.Client.INVENTORY_ITEM_TEXTURE_SCALE);

        Button btn = new Button();
        btn.setPrefWidth(Constants.Client.INVENTORY_BORDER_SIZE);
        btn.setPrefHeight(Constants.Client.INVENTORY_BORDER_SIZE);
        btn.setStyle("-fx-background-color: transparent");
        btn.setOnAction(e ->
        {
            selectItemBox.setOpacity(1);
            selectItemBox.getChildren().clear();
            selectItemBox.getChildren().addAll(
                    new ItemView(itemEntry.type()),
                    ShopMenu.createButtonSell(
                            itemEntry.type(),
                            selectItemBox,
                            this
                    )
            );
        });

        Rectangle rect = new Rectangle(Constants.Client.INVENTORY_BORDER_SIZE, Constants.Client.INVENTORY_BORDER_SIZE);
        rect.setStrokeWidth(4.0);
        rect.setArcWidth(25.0);
        rect.setArcHeight(25.0);
        rect.setMouseTransparent(true);
        LinearGradient gradient = new LinearGradient(0.0, 1.0, 1.0, 0.2, true, CycleMethod.NO_CYCLE,
                new Stop(0.6, Color.color(1.0, 0.8, 0.0, 0.34)),
                new Stop(0.85, Color.color(1.0, 0.8, 0.0, 0.74)),
                new Stop(1.0, Color.WHITE));
        rect.fillProperty().bind(
                Bindings.when(btn.pressedProperty())
                        .then(((Paint) Color.color(1.0, 0.8, 0.0, 0.75)))
                        .otherwise(gradient)
        );
        rect.setStroke(Color.color(0.1, 0.1, 0.1, 0.15));
        rect.visibleProperty().bind(btn.hoverProperty());

        return new BorderStackPane(Constants.Client.INVENTORY_BORDER_SIZE, Constants.Client.INVENTORY_BORDER_SIZE, texture, btn, rect);
    }

    protected BorderStackPane createEmptyEntry()
    {
        return new BorderStackPane(Constants.Client.INVENTORY_BORDER_SIZE, Constants.Client.INVENTORY_BORDER_SIZE);
    }

    public void setPage(int page)
    {
        this.currentPage = Math.clamp(
                page,
                0,
                itemPanes.isEmpty() ?
                        0 :
                        (itemPanes.size() - 1) / ITEMS_PER_PAGE
        );
        this.updatePage();
    }

    public void updatePage()
    {
        this.grid.getChildren().clear();
        int size = itemPanes.size();
        int startIndex = currentPage * ITEMS_PER_PAGE;
        for (int i = 0; i < ITEMS_PER_PAGE; i++)
        {
            int index = startIndex + i;
            int column = i % Constants.Client.INVENTORY_COLS;
            int row = i / Constants.Client.INVENTORY_COLS;
            this.grid.add(index < size ? itemPanes.get(index) : createEmptyEntry(), column, row);
        }
    }

    public void prev()
    {
        this.setPage(currentPage - 1);
    }

    public void next()
    {
        this.setPage(currentPage + 1);
    }
}
