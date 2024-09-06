package dev.csu.survivor.input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import dev.csu.survivor.enums.EntityType;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

public class MouseEventHandler implements EventHandler<MouseEvent>
{
    @Override
    public void handle(MouseEvent event)
    {
        switch (event.getButton())
        {
            case PRIMARY ->
            {
                GameWorld world = FXGL.getGameWorld();
                Point2D center = world
                        .getEntitiesByType(EntityType.PLAYER)
                        .getFirst()
                        .getCenter();
                SpawnData data = new SpawnData();
                data.put("position", center);
                data.put("target", new Point2D(event.getX(), event.getY()));
                world.spawn("test_bullet", data);
            }
        }
    }
}
