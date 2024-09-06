package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.MenuType;

public class MainMenu extends BaseMenu
{
    public MainMenu()
    {
        super(MenuType.MAIN_MENU);
    }

    @Override
    protected void initMenuBox(MenuBox menuBox)
    {
        MenuButton itemOnline = new MenuButton("menu.online");
        itemOnline.setOnAction(e ->
        {
            System.out.println("online");
        });
        menuBox.add(itemOnline);

        super.initMenuBox(menuBox);
    }
}
