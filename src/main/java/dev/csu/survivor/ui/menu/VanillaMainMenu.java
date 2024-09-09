package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.MenuType;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class VanillaMainMenu extends BaseMenu
{
    public VanillaMainMenu()
    {
        super(MenuType.MAIN_MENU);
    }

    @Override
    protected void initMenuBox(MenuBox menuBox)
    {
        MenuButton itemNewGame = new MenuButton("menu.newGame");
        itemNewGame.setOnAction(e -> fireNewGame());
        MenuButton itemOptions = new MenuButton("menu.options");
        itemOptions.setChild(createOptionsMenu());
        MenuButton itemExit = new MenuButton("menu.exit");
        itemExit.setOnAction(e -> fireExit());
        menuBox.add(itemNewGame);
        menuBox.add(itemOptions);
        menuBox.add(itemExit);
    }

    @Override
    protected Node createBackground(double width, double height)
    {
        Rectangle bg = new Rectangle(width, height);
        bg.setFill(Color.rgb(10, 1, 1, 1.0));
        return bg;
    }
}
