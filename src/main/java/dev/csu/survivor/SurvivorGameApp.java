package dev.csu.survivor;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.GameWorld;
import dev.csu.survivor.factory.SurvivorEntityFactory;
import dev.csu.survivor.input.SurvivorGameInput;
import dev.csu.survivor.physics.SurvivorPhysicsHandler;
import dev.csu.survivor.ui.SurvivorGameHud;
import dev.csu.survivor.world.SurvivorGameWorld;

import java.util.Map;

public class SurvivorGameApp extends GameApplication
{
    private SurvivorGameWorld world;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings)
    {
        gameSettings.setTitle(Constants.GameProperties.GAME_NAME);
        gameSettings.setVersion(Constants.GameProperties.GAME_VERSION);
        gameSettings.setWidth(Constants.GameProperties.GAME_SCENE_WIDTH);
        gameSettings.setHeight(Constants.GameProperties.GAME_SCENE_HEIGHT);
        gameSettings.setMainMenuEnabled(Constants.GameProperties.MAIN_MENU_ENABLED);
        gameSettings.setApplicationMode(Constants.GameProperties.APP_MODE);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars)
    {
//        vars.put("death", 0);
    }

    @Override
    protected void initInput()
    {
        new SurvivorGameInput().init(FXGL.getInput());
    }

    @Override
    protected void initGame()
    {
        GameWorld gameWorld = FXGL.getGameWorld();
        gameWorld.addEntityFactory(new SurvivorEntityFactory());
        world = new SurvivorGameWorld(gameWorld);
    }

    @Override
    protected void onUpdate(double tpf)
    {
        world.tick();
    }

    @Override
    protected void initPhysics()
    {
        new SurvivorPhysicsHandler().init(FXGL.getPhysicsWorld());
    }

    @Override
    protected void initUI()
    {
        new SurvivorGameHud().init(FXGL.getGameScene());
    }
}
