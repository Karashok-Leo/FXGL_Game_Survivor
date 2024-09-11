package dev.csu.survivor.ui;

import com.almasb.fxgl.texture.Texture;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.player.InventoryComponent;
import dev.csu.survivor.ui.menu.ShopMenu;
import dev.csu.survivor.world.SurvivorGameWorld;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * 物品栏视图
 * <p>显示以下内容：</p>
 * <p>5x5物品栏位</p>
 * <p>物品详细视图</p>
 * <p>翻页按钮</p>
 */
public class InventoryPane extends HBox implements IPaginationBox
{
    protected List<BorderStackPane> itemPanes;
    protected final GridPane grid;
    protected final VBox selectItemBox;
    private final SimpleIntegerProperty currentPage = new SimpleIntegerProperty(0);

    public InventoryPane()
    {
        super();

        this.grid = new GridPane();
        this.grid.setAlignment(Pos.CENTER);
        this.grid.setHgap(Constants.Client.INVENTORY_ENTRY_SPACING);
        this.grid.setVgap(Constants.Client.INVENTORY_ENTRY_SPACING);

        this.updateInventory();
        this.setPage(0);

        VBox inventoryBox = this.createWholeBox(Constants.Client.INVENTORY_ENTRY_SPACING);

        this.selectItemBox = new VBox();
        this.selectItemBox.setTranslateX(100);

        this.getChildren().addAll(inventoryBox, selectItemBox);
    }

    /**
     * 更新物品栏视图
     */
    public void updateInventory()
    {
        this.itemPanes = SurvivorGameWorld.getPlayer()
                .getComponent(InventoryComponent.class)
                .getInventory()
                .stream()
                .map(this::createInventoryEntry)
                .toList();
    }

    /**
     * 创建单个物品栏位
     *
     * @param itemEntry 实体物品栏项目
     * @return 单个物品栏位的 BorderStackPane 对象
     */
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
                new Stop(0.6, Color.color(0.6, 0.96, 1.0, 0.1)),
                new Stop(0.85, Color.color(0.6, 0.96, 1.0, 0.3)),
                new Stop(1.0, Color.WHITE));
        rect.fillProperty().bind(
                Bindings.when(btn.pressedProperty())
                        .then(((Paint) Color.color(0.6, 0.96, 1.0, 0.3)))
                        .otherwise(gradient)
        );
        rect.setStroke(Color.color(0.1, 0.1, 0.1, 0.15));
        rect.visibleProperty().bind(btn.hoverProperty());

        return new BorderStackPane(Constants.Client.INVENTORY_BORDER_SIZE, Constants.Client.INVENTORY_BORDER_SIZE, texture, btn, rect);
    }

    @Override
    public IntegerProperty currentPageProperty()
    {
        return currentPage;
    }

    @Override
    public BorderStackPane createEmptyEntry()
    {
        return new BorderStackPane(Constants.Client.INVENTORY_BORDER_SIZE, Constants.Client.INVENTORY_BORDER_SIZE);
    }

    @Override
    public GridPane getEntryPane()
    {
        return grid;
    }

    @Override
    public List<? extends Node> getEntries()
    {
        return itemPanes;
    }

    @Override
    public int getColumns()
    {
        return Constants.Client.INVENTORY_COLS;
    }

    @Override
    public int getRows()
    {
        return Constants.Client.INVENTORY_ROWS;
    }
}
