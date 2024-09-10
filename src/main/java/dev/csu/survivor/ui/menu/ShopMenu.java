package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.ui.FXGLButton;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.player.GoldComponent;
import dev.csu.survivor.component.player.InventoryComponent;
import dev.csu.survivor.enums.ItemType;
import dev.csu.survivor.ui.BorderStackPane;
import dev.csu.survivor.ui.GoldView;
import dev.csu.survivor.ui.InventoryPane;
import dev.csu.survivor.ui.ItemView;
import dev.csu.survivor.util.MathUtil;
import dev.csu.survivor.world.SurvivorGameWorld;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.function.Consumer;

public class ShopMenu extends BaseMenu
{
    protected final HBox shopEntries;
    protected final InventoryPane inventoryPane;
    protected final FXGLDefaultMenu.MenuContent shopContent;
    protected final FXGLDefaultMenu.MenuContent inventoryContent;

    public ShopMenu()
    {
        super(MenuType.GAME_MENU);

        this.shopEntries = createShopEntries();
        shopEntries.setTranslateX(-776);
        shopEntries.setTranslateY(-200);

        this.inventoryPane = new InventoryPane();
        inventoryPane.setTranslateX(-544);
        inventoryPane.setTranslateY(-200);

        this.shopContent = new FXGLDefaultMenu.MenuContent(this.shopEntries);
        this.inventoryContent = new FXGLDefaultMenu.MenuContent(this.inventoryPane);

        this.refreshShopEntries();
        this.switchMenuContentTo(this.shopContent);

        GoldView goldView = new GoldView();
        goldView.getLabel().setTextFill(Color.WHITE);
        goldView.setTranslateX(40);
        goldView.setTranslateY(90);
        this.addListener(goldView);
        this.getContentRoot().getChildren().add(goldView);

        getInput().addEventHandler(KeyEvent.ANY, event ->
        {
            if (event.getCode() == FXGL.getSettings().getMenuKey())
                fireResume();
        });
    }

    @Override
    protected String getTitle()
    {
        return "Shop & Inventory";
    }

    @Override
    protected Node createBackground(double width, double height)
    {
        Rectangle bg = new Rectangle(width, height);
        bg.setFill(Color.rgb(10, 1, 1, 0.8));
        return bg;
    }

    @Override
    protected void initMenuBox(MenuBox menuBox)
    {
        MenuButton shop = new MenuButton("menu.shop");
        shop.setMenuContent(() -> this.shopContent, true);

        MenuButton inventory = new MenuButton("menu.inventory");
        inventory.setMenuContent(() -> this.inventoryContent, true);

        MenuButton itemResume = new MenuButton("menu.resume");
        itemResume.setOnAction(e -> fireResume());

        menuBox.add(shop, inventory, itemResume);
    }

    public void refreshShopEntries()
    {
        this.shopEntries.getChildren().clear();
        this.shopEntries.getChildren().addAll(
                MathUtil.weightedRandomSet(4, List.of(ItemType.values()), type -> type.weight)
                        .stream()
                        .map(this::createShopEntry)
                        .toList()
        );
    }

    protected HBox createShopEntries()
    {
        return new HBox();
    }

    protected VBox createShopEntry(ItemType itemType)
    {
        VBox itemBox = new VBox();
        itemBox.setSpacing(Constants.Client.SHOP_ENTRY_OUTER_SPACING);
        itemBox.getChildren().addAll(new ItemView(itemType), createButtonBuy(itemType, itemBox));
        return itemBox;
    }

    public static BorderStackPane createBorderButton(String text, int width, Consumer<Button> handler)
    {
        Button button = new FXGLButton(text);
        button.setFont(FXGL.getUIFactoryService().newFont(Constants.Client.SHOP_ITEM_NAME_FONT));
        button.setAlignment(Pos.CENTER);
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: transparent");
        button.setOnAction(e -> handler.accept(button));
        return new BorderStackPane(width, 40, button);
    }

    public BorderStackPane createButtonBuy(ItemType itemType, VBox itemBox)
    {
        return createBorderButton(
                "%s (-%d G)".formatted(
                        FXGL.localize("menu.buy"),
                        itemType.price
                ),
                Constants.Client.SHOP_ENTRY_WIDTH,
                button ->
                {
                    Entity player = SurvivorGameWorld.getPlayer();
                    GoldComponent golds = player.getComponent(GoldComponent.class);

                    // Determine whether golds is enough
                    if (golds.getValue() < itemType.price) return;

                    // Add the item to the inventory
                    player.getComponent(InventoryComponent.class).addItem(itemType);
                    // Decrease the golds
                    golds.damage(itemType.price);

                    FadeTransition ft = new FadeTransition(Constants.Client.SHOP_ENTRY_FADE_DURATION, itemBox);
                    ft.setToValue(0);
                    ft.play();

                    button.setDisable(true);

                    this.inventoryPane.updateInventory();
                    this.inventoryPane.updatePage();
                }
        );
    }

    public static BorderStackPane createButtonSell(ItemType itemType, VBox itemBox, InventoryPane inventoryPaneToUpdate)
    {
        return createBorderButton(
                "%s (+%d G)".formatted(
                        FXGL.localize("menu.sell"),
                        itemType.price
                ),
                Constants.Client.SHOP_ENTRY_WIDTH,
                button ->
                {
                    Entity player = SurvivorGameWorld.getPlayer();
                    GoldComponent golds = player.getComponent(GoldComponent.class);

                    // Remove the item from the inventory
                    player.getComponent(InventoryComponent.class).removeItem(itemType);
                    // Increase the golds
                    golds.restore(itemType.price);

                    FadeTransition ft = new FadeTransition(Constants.Client.SHOP_ENTRY_FADE_DURATION, itemBox);
                    ft.setToValue(0);
                    ft.play();

                    button.setDisable(true);

                    inventoryPaneToUpdate.updateInventory();
                    inventoryPaneToUpdate.updatePage();
                }
        );
    }

    @Override
    public void onDestroy()
    {
        switchMenuTo(menu);
        switchMenuContentTo(this.shopContent);
    }
}
