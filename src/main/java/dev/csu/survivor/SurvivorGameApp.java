package dev.csu.survivor;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
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
        gameSettings.setTitle("Survivor");
        gameSettings.setWidth(1000);
        gameSettings.setHeight(800);
        gameSettings.setVersion("0.1.0");
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setApplicationMode(ApplicationMode.DEVELOPER);
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
        FXGL.getGameWorld().addEntityFactory(new SurvivorEntityFactory());
        world = new SurvivorGameWorld();
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
