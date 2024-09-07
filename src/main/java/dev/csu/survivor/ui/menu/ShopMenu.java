package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;

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
    protected void initMenuBox(MenuBox menuBox)
    {
        MenuButton back = new MenuButton("menu.back");
        back.setOnAction(e -> FXGL.getSceneService().popSubScene());
        menuBox.add(back);
    }
}
