package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.MenuType;

public class GameOverMenu extends BaseMenu
{
    public GameOverMenu()
    {
        super(MenuType.GAME_MENU);
    }

    @Override
    protected String getTitle()
    {
        return "Game Over";
    }

    @Override
    protected void initMenuBox(MenuBox menuBox)
    {
        menuBox.add(createRestartButton());
        menuBox.add(createExitButton());
    }

    protected MenuButton createRestartButton()
    {
        MenuButton itemRestart = new MenuButton("menu.restart");
        itemRestart.setOnAction(e -> fireNewGame());
        return itemRestart;
    }
}
