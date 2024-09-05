package dev.csu.survivor.factory;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.collision.shapes.CircleShape;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.AnimationComponent;
import dev.csu.survivor.component.MotionComponent;

public class ComponentFactory
{
    /**
     * @return a new AnimationComponent for the animations of player
     */
    public static AnimationComponent newPlayerAnimationComponent()
    {
        return new AnimationComponent(Constants.AnimationMaps.PLAYER_ANIMATION_MAP);
    }

    /**
     * @return a new AnimationComponent for the animations of enemy
     */
    public static AnimationComponent newEnemyAnimationComponent()
    {
        return new AnimationComponent(Constants.AnimationMaps.ENEMY_ANIMATION_MAP);
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

    public static MotionComponent newRandomMotionComponent(double minSpeed, double maxSpeed)
    {
        return new MotionComponent(FXGLMath.random(minSpeed, maxSpeed));
    }
}
