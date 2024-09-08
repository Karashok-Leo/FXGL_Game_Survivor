package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.enums.ItemType;
import dev.csu.survivor.ui.ItemView;
import javafx.scene.layout.HBox;

import java.util.Arrays;
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

    @Override
    protected FXGLDefaultMenu.MenuContent createMenuContent()
    {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        List<ItemView> list = Arrays.stream(ItemType.values()).map(ItemView::new).toList();
        hbox.getChildren().addAll(list);
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
