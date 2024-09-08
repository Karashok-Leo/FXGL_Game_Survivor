package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.ItemType;
import dev.csu.survivor.ui.BorderStackPane;
import dev.csu.survivor.ui.ItemView;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShopMenu extends BaseMenu
{
    public ShopMenu()
    {
        super(MenuType.GAME_MENU);
    }

    @Override
    protected String getTitle()
    {
        return "Shop";
    }

    protected static List<ItemType> randomSelectItem(int n)
    {
        List<ItemType> items = Arrays.asList(ItemType.values());
        Collections.shuffle(items);
        return items.subList(0, n);
    }

    @Override
    protected FXGLDefaultMenu.MenuContent createMenuContent()
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
            BorderStackPane paneBuy = new BorderStackPane(Constants.Client.SHOP_ENTRY_WIDTH, 40, buttonBuy);

            subVBox.getChildren().addAll(itemView, paneBuy);

            hbox.getChildren().add(subVBox);
        }
        hbox.setTranslateX(-480);
        hbox.setTranslateY(-240);
        return new FXGLDefaultMenu.MenuContent(hbox);
    }

    @Override
    protected void initMenuBox(MenuBox menuBox)
    {
        MenuButton back = new MenuButton("menu.back");
        back.setOnAction(e -> FXGL.getSceneService().popSubScene());
        menuBox.add(back);
    }
}
