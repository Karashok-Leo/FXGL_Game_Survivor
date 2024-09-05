package dev.csu.survivor.world;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.time.LocalTimer;
import dev.csu.survivor.Constants;

public class SurvivorGameWorld
{
    private final LocalTimer localTimer;
    private final GameWorld internalWorld;
    private final Entity player;

    public SurvivorGameWorld(GameWorld internalWorld)
    {
        this.localTimer = FXGL.newLocalTimer();
        this.internalWorld = internalWorld;
        this.player = internalWorld.spawn("player", Constants.GameData.PLAYER_SPAWN_POINT);
    }

    public void tick()
    {
        if (localTimer.elapsed(Constants.GameData.ENEMY_SPAWN_DURATION))
        {
            localTimer.capture();
            internalWorld.spawn("enemy", FXGLMath.randomPoint(Constants.GameProperties.GAME_SCENE_RECT));
        }
    }

    public GameWorld getInternalWorld()
    {
        return internalWorld;
    }

    public Entity getPlayer()
    {
        return player;
    }
}
