package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.ui.FXGLButton;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.GoldComponent;
import dev.csu.survivor.component.InventoryComponent;
import dev.csu.survivor.enums.ItemType;
import dev.csu.survivor.ui.BorderStackPane;
import dev.csu.survivor.ui.GoldView;
import dev.csu.survivor.ui.InventoryPane;
import dev.csu.survivor.ui.ItemView;
import dev.csu.survivor.world.SurvivorGameWorld;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.*;
import java.util.function.Consumer;

public class ShopMenu extends BaseMenu
{
    protected final InventoryPane inventoryPane;
    protected final HBox shopEntries;
    protected final FXGLDefaultMenu.MenuContent inventoryContent;
    protected final FXGLDefaultMenu.MenuContent shopContent;

    public ShopMenu()
    {
        super(MenuType.GAME_MENU);

        this.shopEntries = createShopEntries();
        shopEntries.setTranslateX(-640);
        shopEntries.setTranslateY(-240);

        this.inventoryPane = new InventoryPane();
        inventoryPane.setTranslateX(-570);
        inventoryPane.setTranslateY(-240);

        this.inventoryContent = new FXGLDefaultMenu.MenuContent(this.inventoryPane);
        this.shopContent = new FXGLDefaultMenu.MenuContent(this.shopEntries);

        this.switchMenuContentTo(this.shopContent);

        GoldView goldView = new GoldView();
        goldView.getLabel().setTextFill(Color.WHITE);
        goldView.setTranslateX(40);
        goldView.setTranslateY(90);
        this.addListener(goldView);
        this.getContentRoot().getChildren().add(goldView);
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

    /**
     * @param n Should be greater than the length of the values in ItemType
     * @return A set containing n random item types, according to the weights of types
     */
    @SuppressWarnings("SameParameterValue")
    protected static Set<ItemType> randomSelectItem(int n)
    {
        Random random = FXGLMath.getRandom();

        List<ItemType> pool = new ArrayList<>(List.of(ItemType.values()));

        int totalWeight = 0;
        for (ItemType itemType : pool)
            totalWeight += itemType.weight;

        int size = pool.size();

        if (totalWeight <= 0 || size <= n) throw new AssertionError();

        Set<ItemType> result = new HashSet<>();
        while (result.size() < n)
        {
            int randomInt = random.nextInt(totalWeight);
            for (ItemType itemType : pool)
            {
                randomInt -= itemType.weight;
                if (randomInt < 0)
                {
                    result.add(itemType);
                    break;
                }
            }
        }

        return result;
    }

    @Override
    protected void initMenuBox(MenuBox menuBox)
    {
        MenuButton back = new MenuButton("menu.back");
        back.setOnAction(e -> FXGL.getSceneService().popSubScene());

        MenuButton inventory = new MenuButton("menu.inventory");
        inventory.setMenuContent(() -> this.inventoryContent, true);

        MenuButton shop = new MenuButton("menu.shop");
        shop.setMenuContent(() -> this.shopContent, true);

        menuBox.add(back, inventory, shop);
    }

    protected HBox createShopContent()
    {
        HBox shopEntries = createShopEntries();
        shopEntries.setTranslateX(-600);
        shopEntries.setTranslateY(-240);
        return shopEntries;
    }

    protected HBox createShopEntries()
    {
        HBox hbox = new HBox();
        hbox.setSpacing(Constants.Client.SHOP_ENTRY_OUTER_SPACING);

        hbox.getChildren().addAll(
                randomSelectItem(4)
                        .stream()
                        .map(this::createShopEntry)
                        .toList()
        );

        return hbox;
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
        button.setFont(Font.font(Constants.Client.SHOP_ITEM_NAME_FONT));
        button.setAlignment(Pos.CENTER);
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: transparent");
        button.setOnAction(e -> handler.accept(button));
        return new BorderStackPane(width, 40, button);
    }

    public BorderStackPane createButtonBuy(ItemType itemType, VBox itemBox)
    {
        return createBorderButton(
                "%s (-%d Golds)".formatted(
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
                "%s (+%d Golds)".formatted(
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
}
