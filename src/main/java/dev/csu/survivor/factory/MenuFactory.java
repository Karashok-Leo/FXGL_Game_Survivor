package dev.csu.survivor.factory;

import com.almasb.fxgl.app.scene.*;
import dev.csu.survivor.ui.menu.GameMenu;
import dev.csu.survivor.ui.menu.MainMenu;
import org.jetbrains.annotations.NotNull;

public class MenuFactory extends SceneFactory
{
    @NotNull
    @Override
    public FXGLMenu newGameMenu()
    {
        return new GameMenu();
    }

    @NotNull
    @Override
    public IntroScene newIntro()
    {
        return super.newIntro();
    }

    @NotNull
    @Override
    public LoadingScene newLoadingScene()
    {
        return super.newLoadingScene();
    }

    @NotNull
    @Override
    public FXGLMenu newMainMenu()
    {
        return new MainMenu();
    }

    @NotNull
    @Override
    public StartupScene newStartup(int width, int height)
    {
        return super.newStartup(width, height);
    }
}
