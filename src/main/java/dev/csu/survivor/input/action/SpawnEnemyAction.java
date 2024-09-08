package dev.csu.survivor.input.action;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.net.Connection;

public class SpawnEnemyAction extends UserAction
{
    private Connection<Bundle> connection;

    public SpawnEnemyAction(Connection<Bundle> connection)
    {
        super("SPAWN_ENEMY");
        this.connection = connection;
    }

    @Override
    protected void onActionEnd()
    {
        Input input = FXGL.getInput();
        var bundle = new Bundle("spawn_enemy");
        Vec2 spawnPosition = new Vec2(input.getMouseXWorld(), input.getMouseYWorld());
        bundle.put("spawn_position", spawnPosition);
        connection.send(bundle);
    }
}
