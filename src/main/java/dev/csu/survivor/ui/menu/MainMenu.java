package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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

        MenuButton itemOptions = new MenuButton("menu.options");
        itemOptions.setChild(createOptionsMenu());

        MenuButton itemOnline = new MenuButton("menu.online");
        itemOnline.setOnAction(e -> System.out.println("online"));

        MenuButton itemExit = new MenuButton("menu.exit");
        itemExit.setOnAction(e -> fireExit());

        menuBox.add(itemNewGame);
        menuBox.add(itemOptions);
        menuBox.add(itemOnline);
        menuBox.add(itemExit);
    }

    @Override
    protected Node createBackground(double width, double height)
    {
        Rectangle bg = new Rectangle(width, height);
        bg.setFill(Color.rgb(10, 1, 1, 1.0));
        return bg;
    }

    @Override
    protected String getTitle()
    {
        return FXGL.getSettings().getTitle();
    }
}
