package dev.csu.survivor.input.action;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import dev.csu.survivor.world.SurvivorGameWorld;
import javafx.geometry.Point2D;

@Deprecated
public class ShootBulletAction extends UserAction
{
    public ShootBulletAction()
    {
        super("Shoot Bullet");
    }

    @Override
    protected void onActionEnd()
    {
        GameWorld world = FXGL.getGameWorld();
        Input input = FXGL.getInput();
        Entity player = SurvivorGameWorld.getPlayer();
        Point2D center = player.getCenter();
        SpawnData data = new SpawnData();
        data.put("position", center);
        data.put("target", new Point2D(input.getMouseXWorld(), input.getMouseYWorld()));
        data.put("owner", player);
        world.spawn("test_bullet", data);
    }
}
