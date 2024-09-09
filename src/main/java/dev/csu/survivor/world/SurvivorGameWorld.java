package dev.csu.survivor.world;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.time.LocalTimer;
import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.EntityType;

public class SurvivorGameWorld
{
    private final LocalTimer localTimer;
    private final GameWorld internalWorld;
    private final Entity player;
    private final Entity land;

    public SurvivorGameWorld(GameWorld internalWorld)
    {
        this.localTimer = FXGL.newLocalTimer();
        this.localTimer.capture();
        this.internalWorld = internalWorld;
        this.player = this.internalWorld.spawn("player", Constants.Common.PLAYER_SPAWN_POINT);
        this.land = this.internalWorld.spawn("land");

        // create random bushes
        for (int i = 0; i < Constants.Common.RANDOM_BUSH_COUNTS; i++)
            this.internalWorld.spawn("bush", FXGLMath.randomPoint(Constants.GAME_SCENE_RECT));

        this.internalWorld.spawn("boomerang");
    }

    public void tick()
    {
        if (localTimer.elapsed(Constants.Common.ENEMY_SPAWN_DURATION))
        {
            localTimer.capture();
            internalWorld.spawn("melee_enemy", FXGLMath.randomPoint(Constants.GAME_SCENE_RECT));
        }
    }

    public static Entity getPlayer()
    {
        return FXGL.getGameWorld()
                .getSingleton(EntityType.PLAYER);
    }
}
