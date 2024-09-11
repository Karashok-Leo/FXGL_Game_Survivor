package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import com.almasb.fxgl.app.scene.MenuType;
import dev.csu.survivor.util.StyleUtil;
import dev.csu.survivor.world.SurvivorGameWorld;
import javafx.scene.control.Label;

public class GameOverMenu extends BaseMenu
{
    protected FXGLDefaultMenu.MenuContent waveContent;

    public GameOverMenu()
    {
        super(MenuType.GAME_MENU);
        this.waveContent = createWaveContent();
        this.switchMenuContentTo(waveContent);
    }

    @Override
    protected String getTitle()
    {
        return "Game Over";
    }

    protected FXGLDefaultMenu.MenuContent createWaveContent()
    {
        Label label = new Label(
                "You have survived %d waves!".formatted(
                        SurvivorGameWorld.getInstance()
                                .waveProperty()
                                .getValue()
                )
        );
        label.setTranslateX(-505);
        StyleUtil.setLabelStyle(label);
        return new FXGLDefaultMenu.MenuContent(label);
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
