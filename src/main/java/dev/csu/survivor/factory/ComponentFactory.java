package dev.csu.survivor.factory;

import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.collision.shapes.CircleShape;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import dev.csu.survivor.component.AnimationComponent;
import dev.csu.survivor.enums.EntityStates;

public class ComponentFactory
{
    public static final AnimationComponent.AnimationMap PLAYER_ANIMATION_MAP = new AnimationComponent.AnimationMap(
            "player",
            new AnimationComponent.StateEntry(EntityStates.IDLE, 2, new int[]{4, 12, 12, 12}),
            new AnimationComponent.StateEntry(EntityStates.RUN, 0.8, 8),
            new AnimationComponent.StateEntry(EntityStates.HURT, 0.3, 5),
            new AnimationComponent.StateEntry(EntityStates.DEATH, 0.9, 7)
    );

    public static final AnimationComponent.AnimationMap ENEMY_ANIMATION_MAP = new AnimationComponent.AnimationMap(
            "enemy",
            new AnimationComponent.StateEntry(EntityStates.IDLE, 0.8, 8),
            new AnimationComponent.StateEntry(EntityStates.RUN, 0.8, 8),
            new AnimationComponent.StateEntry(EntityStates.ATTACK, 0.8, 8),
            new AnimationComponent.StateEntry(EntityStates.HURT, 0.3, 6),
            new AnimationComponent.StateEntry(EntityStates.DEATH, 1.2, 8)
    );

    public static AnimationComponent newPlayerAnimationComponent()
    {
        return new AnimationComponent(PLAYER_ANIMATION_MAP);
    }

    public static AnimationComponent newEnemyAnimationComponent()
    {
        return new AnimationComponent(ENEMY_ANIMATION_MAP);
    }

    public static PhysicsComponent newPhysicsComponent(float radius)
    {
        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.setBodyType(BodyType.DYNAMIC);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density(0.8F);
        fixtureDef.shape(new CircleShape(radius));
        physicsComponent.setFixtureDef(fixtureDef);
        return physicsComponent;
    }
}
