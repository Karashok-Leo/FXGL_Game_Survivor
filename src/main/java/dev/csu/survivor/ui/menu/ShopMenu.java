package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.InventoryComponent;
import dev.csu.survivor.enums.ItemType;
import dev.csu.survivor.ui.BorderStackPane;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShopMenu extends BaseMenu
{
    protected final FXGLDefaultMenu.MenuContent shopContent;
    protected final FXGLDefaultMenu.MenuContent inventoryContent;

    public ShopMenu()
    {
        super(MenuType.GAME_MENU);
        this.shopContent = createShopContent();
        this.inventoryContent = createInventoryContent();
        this.switchMenuContentTo(this.shopContent);
    }

    @Override
    protected String getTitle()
    {
        return "Shop";
    }

    @Override
    protected Node createBackground(double width, double height)
    {
        Rectangle bg = new Rectangle(width, height);
        bg.setFill(Color.rgb(10, 1, 1, 0.8));
        return bg;
    }

    protected static List<ItemType> randomSelectItem(int n)
    {
        List<ItemType> items = Arrays.asList(ItemType.values());
        Collections.shuffle(items);
        return items.subList(0, n);
    }

    @Override
    protected void initMenuBox(MenuBox menuBox)
    {
        MenuButton back = new MenuButton("menu.back");
        back.setOnAction(e -> FXGL.getSceneService().popSubScene());

        MenuButton inventory = new MenuButton("menu.inventory");
        inventory.setMenuContent(() -> this.shopContent, true);

        MenuButton shop = new MenuButton("menu.shop");
        shop.setMenuContent(() -> this.inventoryContent, true);

        menuBox.add(back, inventory, shop);
    }

    protected FXGLDefaultMenu.MenuContent createShopContent()
    {
        HBox hbox = new HBox();
        hbox.setSpacing(Constants.Client.SHOP_ENTRY_OUTER_SPACING);

        for (ItemType itemType : randomSelectItem(3))
        {
            VBox subVBox = new VBox();
            subVBox.setSpacing(Constants.Client.SHOP_ENTRY_OUTER_SPACING);

            ItemView itemView = new ItemView(itemType);

            Button buttonBuy = FXGL.getUIFactoryService().newButton(FXGL.localizedStringProperty("menu.buy"));
            buttonBuy.setAlignment(Pos.CENTER);
            buttonBuy.setTextFill(Color.WHITE);
            buttonBuy.setStyle("-fx-background-color: transparent");

            buttonBuy.setOnAction(e ->
            {
                buttonBuy.setDisable(true);
                InventoryComponent inv = SurvivorGameWorld.getPlayer().getComponent(InventoryComponent.class);
                inv.addItem(itemType.createItem());

                FadeTransition ft = new FadeTransition(Constants.Client.SHOP_ENTRY_FADE_DURATION, subVBox);
                ft.setToValue(0);
                ft.setOnFinished(event -> subVBox.setVisible(false));
                ft.play();
            });

            BorderStackPane paneBuy = new BorderStackPane(Constants.Client.SHOP_ENTRY_WIDTH, 40, buttonBuy);

            subVBox.getChildren().addAll(itemView, paneBuy);

            hbox.getChildren().add(subVBox);
        }
        hbox.setTranslateX(-480);
        hbox.setTranslateY(-240);
        return new FXGLDefaultMenu.MenuContent(hbox);
    }

    protected FXGLDefaultMenu.MenuContent createInventoryContent()
    {
        return EMPTY;
    }
}
