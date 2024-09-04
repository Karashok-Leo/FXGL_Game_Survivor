package dev.csu.survivor.component;

import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.enums.EntityType;
import javafx.geometry.Point2D;

public class EnemyMotionComponent extends MotionComponent
{
    public EnemyMotionComponent(double speed)
    {
        super(speed);
    }

    @Override
    public void onUpdate(double tpf)
    {
        Point2D center = FXGL.getGameWorld()
                .getEntitiesByType(EntityType.PLAYER)
                .getFirst()
                .getPosition();
        this.getVelocity().set(center.subtract(entity.getPosition()));
        super.onUpdate(tpf);
    }
}
