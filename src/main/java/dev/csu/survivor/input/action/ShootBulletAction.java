package dev.csu.survivor.input.action;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import dev.csu.survivor.enums.EntityType;
import javafx.geometry.Point2D;

public class ShootBulletAction extends UserAction
{
    public ShootBulletAction()
    {
        super("SHOOT");
    }

    @Override
    protected void onActionEnd()
    {
        GameWorld world = FXGL.getGameWorld();
        Input input = FXGL.getInput();
        Point2D center = world
                .getEntitiesByType(EntityType.PLAYER)
                .getFirst()
                .getCenter();
        SpawnData data = new SpawnData();
        data.put("position", center);
        data.put("target", new Point2D(input.getMouseXWorld(), input.getMouseYWorld()));
        world.spawn("test_bullet", data);

        // use for coin anim testing
//        FXGL.entityBuilder()
//                .at(input.getMouseXWorld(), input.getMouseYWorld())
//                .bbox(new HitBox(BoundingShape.box(7, 7)))
//                .with(new CoinAnimationComponent())
//                .buildAndAttach();
    }
}
