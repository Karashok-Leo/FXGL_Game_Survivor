package dev.csu.survivor;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Map;

public class SurvivorGameApp extends GameApplication
{
    private Entity player;

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
        vars.put("death", 0);
    }

    @Override
    protected void initInput()
    {
        FXGL.onKey(KeyCode.W, () -> player.translate(0, -4));
        FXGL.onKey(KeyCode.S, () -> player.translate(0, 4));
        FXGL.onKey(KeyCode.A, () -> player.translate(-4, 0));
        FXGL.onKey(KeyCode.D, () -> player.translate(4, 0));
        FXGL.onKey(KeyCode.R, () -> FXGL.inc("death", 1));
    }

    @Override
    protected void initGame()
    {
        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(500, 400)
                .viewWithBBox(new Rectangle(40, 40))
                .collidable()
                .buildAndAttach();
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

        textPixels.textProperty().bind(
                FXGL.getWorldProperties().intProperty("death").asString()
        );

        FXGL.getGameScene().addUINode(textPixels); // add to the scene graph
    }
}
