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
        MenuButton itemNewGame = new MenuButton("menu.newGame");
        itemNewGame.setOnAction(e -> fireNewGame());

        MenuButton itemExit = new MenuButton("menu.exit");
        itemExit.setOnAction(e -> fireExit());

        MenuButton itemOptions = new MenuButton("menu.options");
        itemOptions.setOnAction(e -> System.out.println("online"));

        MenuButton itemOnline = new MenuButton("menu.online");
        itemOnline.setOnAction(e -> System.out.println("online"));

        menuBox.add(itemNewGame);
        menuBox.add(itemOptions);
        menuBox.add(itemOnline);
        menuBox.add(itemExit);

        itemOptions.setChild(createOptionsMenu());
    }
}
