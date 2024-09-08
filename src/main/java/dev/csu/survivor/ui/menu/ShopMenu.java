package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.layout.HBox;

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
        hbox.getChildren().addAll();
        return new FXGLDefaultMenu.MenuContent();
    }

    @Override
    protected void initMenuBox(MenuBox menuBox)
    {
        MenuButton back = new MenuButton("menu.back");
        back.setOnAction(e -> FXGL.getSceneService().popSubScene());
        menuBox.add(back);
    }
}
