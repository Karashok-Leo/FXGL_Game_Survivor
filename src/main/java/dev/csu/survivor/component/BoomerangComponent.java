package dev.csu.survivor.component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.EntityType;
import dev.csu.survivor.world.SurvivorGameWorld;
import javafx.geometry.Point2D;

import java.util.List;

public class BoomerangComponent extends Component
{
    protected List<Entity> boomerangEntities;

    public BoomerangComponent()
    {
    }

    @Override
    public void onAdded()
    {
        super.onAdded();
    }

    @Override
    public void onUpdate(double tpf)
    {
        Point2D center = SurvivorGameWorld
                .getPlayer()
                .getCenter();
        double deviationX = -15;
        double deviationY = -15;
        double radius = Constants.Common.BOOMERANG_REVOLUTION_RADIUS;
        double angle = FXGL.getGameTimer().getNow() * Constants.Common.BOOMERANG_REVOLUTION_SPEED;
        List<Entity> boomerangs = FXGL.getGameWorld().getEntitiesByType(EntityType.BOOMERANG);

        double angleIncrement = 2 * Math.PI / boomerangs.size();
        double currentAngle = angle;

        for (Entity boomerang : boomerangs) {
            boomerang.rotateBy(tpf * Constants.Common.BOOMERANG_ROTATION_SPEED);

            double x = deviationX + center.getX() + radius * Math.cos(currentAngle);
            double y = deviationY + center.getY() + radius * Math.sin(currentAngle);
            Point2D boomerangPosition = new Point2D(x, y);
            boomerang.setPosition(boomerangPosition);

            currentAngle += angleIncrement;
        }
    }
}
