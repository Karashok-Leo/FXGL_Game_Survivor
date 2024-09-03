package dev.csu.survivor;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.physics.CollisionHandler;
import dev.csu.survivor.action.MoveAction;
import dev.csu.survivor.enums.Direction;
import dev.csu.survivor.enums.EntityType;
import dev.csu.survivor.world.SurvivorGameWorld;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

import java.util.Map;

public class SurvivorGameApp extends GameApplication
{
    private SurvivorGameWorld world;

    @Override
    protected void initSettings(GameSettings gameSettings)
    {
        gameSettings.setTitle("Survivor");
        gameSettings.setWidth(1000);
        gameSettings.setHeight(800);
        gameSettings.setVersion("0.1.0");
        gameSettings.setMainMenuEnabled(true);
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars)
    {
//        vars.put("death", 0);
    }

    @Override
    protected void initInput()
    {
        Input input = FXGL.getInput();
        input.addAction(new MoveAction("UP", () -> world.getPlayer(), Direction.UP), KeyCode.W);
        input.addAction(new MoveAction("DOWN", () -> world.getPlayer(), Direction.DOWN), KeyCode.S);
        input.addAction(new MoveAction("LEFT", () -> world.getPlayer(), Direction.LEFT), KeyCode.A);
        input.addAction(new MoveAction("RIGHT", () -> world.getPlayer(), Direction.RIGHT), KeyCode.D);
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
        FXGL.getPhysicsWorld().addCollisionHandler(
                new CollisionHandler(
                        EntityType.PLAYER,
                        EntityType.ENEMY
                )
                {
                    @Override
                    protected void onCollision(Entity a, Entity b)
                    {
                        b.removeFromWorld();
                    }
                }
        );
    }

    @Override
    protected void initUI()
    {
        Text textPixels = new Text();
        textPixels.setTranslateX(50); // x = 50
        textPixels.setTranslateY(100); // y = 100

//        textPixels.textProperty().bind(
//                FXGL.getWorldProperties().intProperty("death").asString()
//        );

        FXGL.getGameScene().addUINode(textPixels); // add to the scene graph
    }
}
