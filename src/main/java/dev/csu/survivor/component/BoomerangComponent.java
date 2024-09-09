package dev.csu.survivor.component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.RechargeableIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import dev.csu.survivor.Constants;
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
                        .view("item/boomerang_attack.png")
                        .with(new OwnableComponent(() -> entity))
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

        Point2D center = this.entity.getCenter();
        double deviationX = -15;
        double deviationY = -15;
        double radius = Constants.Common.BOOMERANG_REVOLUTION_RADIUS;
        double angle = FXGL.getGameTimer().getNow() * Constants.Common.BOOMERANG_REVOLUTION_SPEED;

        double angleIncrement = 2 * Math.PI / boomerangs.size();
        double currentAngle = angle;

        for (Entity boomerang : boomerangs)
        {
            boomerang.rotateBy(tpf * Constants.Common.BOOMERANG_ROTATION_SPEED);

            double x = deviationX + center.getX() + radius * Math.cos(currentAngle);
            double y = deviationY + center.getY() + radius * Math.sin(currentAngle);
            Point2D boomerangPosition = new Point2D(x, y);
            boomerang.setPosition(boomerangPosition);

            currentAngle += angleIncrement;
        }
    }
}
