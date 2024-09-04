package dev.csu.survivor;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import dev.csu.survivor.action.MoveAction;
import dev.csu.survivor.component.AttackHurtComponent;
import dev.csu.survivor.enums.Direction;
import dev.csu.survivor.enums.EntityType;
import dev.csu.survivor.factory.SurvivorEntityFactory;
import dev.csu.survivor.world.SurvivorGameWorld;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

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
        Input input = FXGL.getInput();
        input.addAction(new MoveAction(Direction.UP), KeyCode.W);
        input.addAction(new MoveAction(Direction.DOWN), KeyCode.S);
        input.addAction(new MoveAction(Direction.LEFT), KeyCode.A);
        input.addAction(new MoveAction(Direction.RIGHT), KeyCode.D);
        input.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
        {
            switch (e.getButton())
            {
                case PRIMARY ->
                {
                    FXGL.getGameWorld().spawn("enemy", e.getX(), e.getY());
                }
            }
        });
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
        PhysicsWorld physicsWorld = FXGL.getPhysicsWorld();

        physicsWorld.setGravity(0, 0);
        physicsWorld.addCollisionHandler(
                new CollisionHandler(
                        EntityType.PLAYER,
                        EntityType.ENEMY
                )
                {
                    @Override
                    protected void onCollision(Entity a, Entity b)
                    {
                        b.getComponent(AttackHurtComponent.class).attack(a);
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
