package dev.csu.survivor.ui;

import com.almasb.fxgl.texture.Texture;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.InventoryComponent;
import dev.csu.survivor.world.SurvivorGameWorld;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

import java.util.List;

public class InventoryPane extends GridPane
{
    private static final int ITEMS_PER_PAGE = Constants.Client.INVENTORY_COLS * Constants.Client.INVENTORY_ROWS;

    private int currentPage;
    protected List<BorderStackPane> itemPanes;

    public InventoryPane()
    {
        super();
        this.setAlignment(Pos.CENTER);
        this.setHgap(Constants.Client.SHOP_ENTRY_OUTER_SPACING);
        this.setVgap(Constants.Client.SHOP_ENTRY_OUTER_SPACING);
        this.itemPanes = SurvivorGameWorld.getPlayer()
                .getComponent(InventoryComponent.class)
                .getInventory()
                .stream()
                .map(this::createInventoryEntry)
                .toList();
        this.setPage(0);
    }

    protected BorderStackPane createInventoryEntry(InventoryComponent.ItemEntry itemEntry)
    {
        Texture texture = itemEntry.type().getTexture().copy();
        texture.setScaleX(Constants.Client.INVENTORY_ITEM_TEXTURE_SCALE);
        texture.setScaleY(Constants.Client.INVENTORY_ITEM_TEXTURE_SCALE);
        return new BorderStackPane(Constants.Client.INVENTORY_BORDER_SIZE, Constants.Client.INVENTORY_BORDER_SIZE, texture);
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

    protected void updatePage()
    {
        this.getChildren().clear();
        int size = itemPanes.size();
        int startIndex = currentPage * ITEMS_PER_PAGE;
        for (int i = 0; i < ITEMS_PER_PAGE; i++)
        {
            int index = startIndex + i;
            int column = i % Constants.Client.INVENTORY_COLS;
            int row = i / Constants.Client.INVENTORY_COLS;
            this.add(index < size ? itemPanes.get(index) : createEmptyEntry(), column, row);
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
