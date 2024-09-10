package dev.csu.survivor.component.player;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.RechargeableIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.base.OwnableComponent;
import dev.csu.survivor.component.misc.BoomerangSelfComponent;
import dev.csu.survivor.enums.EntityType;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class BoomerangComponent extends RechargeableIntComponent
{
    protected List<Entity> boomerangs = new ArrayList<>();
    protected List<Entity> boomerangsToRemove = new ArrayList<>();

    public BoomerangComponent()
    {
        super(100);
    }

    protected void updateBoomerangs()
    {
        int newNum = this.getValue();
        if (newNum > boomerangs.size())
        {
            for (int i = 0; i < newNum - boomerangs.size(); i++)
            {
                Entity boomerang = FXGL.entityBuilder()
                        .type(EntityType.BOOMERANG)
                        .at(entity.getCenter())
                        .bbox(new HitBox(BoundingShape.circle(Constants.Common.BOOMERANG_HIT_BOX_RADIUS)))
                        .with(new OwnableComponent(() -> entity))
                        .with(new BoomerangSelfComponent())
                        .collidable()
                        .build();
                this.boomerangs.add(boomerang);
            }
        } else
        {
            for (int i = 0; i < boomerangs.size() - newNum; i++)
            {
                Entity toRemove = this.boomerangs.removeLast();
                this.boomerangsToRemove.add(toRemove);
            }
        }
    }

    @Override
    public void onAdded()
    {
        this.valueProperty().addListener((observable, oldValue, newValue) ->
                updateBoomerangs());
        this.setValue(1);
    }

    @Override
    public void onUpdate(double tpf)
    {
        for (Entity boomerang : this.boomerangs)
            if (!boomerang.isActive())
                this.entity.getWorld().addEntity(boomerang);

        for (Entity boomerang : this.boomerangsToRemove)
            boomerang.removeFromWorld();
        this.boomerangsToRemove.clear();

        Point2D center = this.entity.getPosition();
        double radius = Constants.Common.BOOMERANG_REVOLUTION_RADIUS;
        double angle = FXGL.getGameTimer().getNow() * Constants.Common.BOOMERANG_REVOLUTION_SPEED;

        double angleStep = 2 * Math.PI / boomerangs.size();
        double currentAngle = angle;

        for (Entity boomerang : boomerangs)
        {
            double x = center.getX() + radius * Math.cos(currentAngle);
            double y = center.getY() + radius * Math.sin(currentAngle);
            boomerang.setPosition(x, y);

            currentAngle += angleStep;
        }
    }
}
