package dev.csu.survivor;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.GameWorld;
import dev.csu.survivor.factory.MenuFactory;
import dev.csu.survivor.factory.SurvivorEntityFactory;
import dev.csu.survivor.input.SurvivorGameInput;
import dev.csu.survivor.physics.SurvivorPhysicsHandler;
import dev.csu.survivor.ui.SurvivorGameHud;
import dev.csu.survivor.world.SurvivorGameWorld;

/**
 * 游戏入口类
 */
public class SurvivorGameApp extends GameApplication
{
    private SurvivorGameWorld world;

    public static void main(String[] args)
    {
        launch(args);
    }

    public SurvivorGameWorld getWorld()
    {
        return world;
    }

    /**
     * 初始化游戏设置
     *
     * @param gameSettings 游戏设置
     */
    @Override
    protected void initSettings(GameSettings gameSettings)
    {
        gameSettings.setTitle(Constants.GAME_NAME);
        gameSettings.setVersion(Constants.GAME_VERSION);
        gameSettings.setWidth(Constants.GAME_SCENE_WIDTH);
        gameSettings.setHeight(Constants.GAME_SCENE_HEIGHT);
        gameSettings.setMainMenuEnabled(Constants.MAIN_MENU_ENABLED);
        gameSettings.setDeveloperMenuEnabled(Constants.DEVELOPER_MENU_ENABLED);
        gameSettings.setApplicationMode(Constants.APP_MODE);
        gameSettings.setSceneFactory(new MenuFactory());
        gameSettings.setFontGame("mojangles.ttf");
        gameSettings.setFontMono("mojangles.ttf");
        gameSettings.setFontText("mojangles.ttf");
        gameSettings.setFontUI("mojangles.ttf");
    }

    /**
     * 初始化控制交互行为
     */
    @Override
    protected void initInput()
    {
        new SurvivorGameInput().init(FXGL.getInput());
    }

    /**
     * 初始化游戏
     */
    @Override
    protected void initGame()
    {
        GameWorld gameWorld = FXGL.getGameWorld();
        gameWorld.addEntityFactory(new SurvivorEntityFactory());
        world = new SurvivorGameWorld(gameWorld);
    }

    /**
     * 游戏帧方法
     *
     * @param tpf time per frame
     */
    @Override
    protected void onUpdate(double tpf)
    {
        world.tick();
    }

    /**
     * 初始化游戏物理设计
     */
    @Override
    protected void initPhysics()
    {
        new SurvivorPhysicsHandler().init(FXGL.getPhysicsWorld());
    }

    /**
     * 初始化游戏 UI
     */
    @Override
    protected void initUI()
    {
        new SurvivorGameHud().init(FXGL.getGameScene());
    }
}
