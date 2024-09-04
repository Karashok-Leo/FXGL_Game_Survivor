package dev.csu.survivor;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.state.StateComponent;
import dev.csu.survivor.component.EnemyAnimationComponent;
import dev.csu.survivor.component.EnemyMotionComponent;
import dev.csu.survivor.component.MotionComponent;
import dev.csu.survivor.component.PlayerAnimationComponent;
import dev.csu.survivor.enums.EntityStates;
import dev.csu.survivor.enums.EntityType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SurvivorEntityFactory implements EntityFactory
{
    @Spawns("player")
    public Entity newPlayer(SpawnData data)
    {
        return FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(data.getX(), data.getY())
                .with(new StateComponent(EntityStates.IDLE))
                .with(new MotionComponent(2))
                .with(new PlayerAnimationComponent())
                .viewWithBBox(new Circle(7, Color.TRANSPARENT))
                .collidable()
                .buildAndAttach();
    }

    @Spawns("enemy")
    public Entity newEnemy(SpawnData data)
    {
        return FXGL.entityBuilder()
                .type(EntityType.ENEMY)
                .at(data.getX(), data.getY())
                .with(new StateComponent(EntityStates.RUN))
                .with(new EnemyMotionComponent(0.8))
                .with(new EnemyAnimationComponent())
                .viewWithBBox(new Circle(9, Color.TRANSPARENT))
                .collidable()
                .buildAndAttach();
    }
}
