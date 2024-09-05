package dev.csu.survivor.input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import dev.csu.survivor.enums.EntityType;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MouseEventHandler implements EventHandler<MouseEvent>
{
    @Override
    public void handle(MouseEvent event)
    {
        switch (event.getButton())
        {
            case PRIMARY ->
            {
                Point2D center = FXGL.getGameWorld()
                        .getEntitiesByType(EntityType.PLAYER)
                        .getFirst()
                        .getCenter();
                Point2D subtract = center.subtract(event.getX(), event.getY()).multiply(-1);
                FXGL.entityBuilder()
                        .type(EntityType.BULLET)
                        .at(center)
                        .viewWithBBox(new Circle(30, Color.RED))
                        .with(new ProjectileComponent(subtract, 600))
                        .with(new OffscreenCleanComponent())
                        .collidable()
                        .buildAndAttach();
            }
        }
    }
}
