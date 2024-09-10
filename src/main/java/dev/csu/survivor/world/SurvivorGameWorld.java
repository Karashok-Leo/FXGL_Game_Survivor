package dev.csu.survivor.world;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.time.LocalTimer;
import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.EnemyType;
import dev.csu.survivor.enums.EntityType;
import dev.csu.survivor.util.MathUtil;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

import java.util.List;

public class SurvivorGameWorld
{
    private final LocalTimer localTimer;
    private final LocalTimer waveTimer;
    private final GameWorld internalWorld;
    private final SimpleIntegerProperty wave;
    private final Entity player;
    private final Entity land;

    public SurvivorGameWorld(GameWorld internalWorld)
    {
        this.localTimer = FXGL.newLocalTimer();
        this.localTimer.capture();
        this.waveTimer = FXGL.newLocalTimer();
        this.waveTimer.capture();
        this.internalWorld = internalWorld;
        this.wave = new SimpleIntegerProperty(1);
        this.player = this.internalWorld.spawn("player", Constants.Common.PLAYER_SPAWN_POINT);
        this.land = this.internalWorld.spawn("land");

        // create random bushes
        for (int i = 0; i < Constants.Common.RANDOM_BUSH_COUNTS; i++)
            this.internalWorld.spawn("bush", FXGLMath.randomPoint(Constants.GAME_SCENE_RECT));

        this.internalWorld.getEntitiesByType(EnemyType.values()).size();
    }

    public void tick()
    {
        if (localTimer.elapsed(Constants.Common.ENEMY_SPAWN_DURATION))
        {
            localTimer.capture();
            for (int i = 0; i < FXGLMath.random(1, Constants.Common.MAX_ENEMIES_SPAWNED_AT_ONCE); i++)
            {
                EnemyType enemyType = MathUtil.weightedRandom(List.of(EnemyType.values()), type -> type.weight);
                spawn(enemyType.spawnName);
            }
        }

        if (waveTimer.elapsed(Duration.minutes(2)))
        {
            wave.set(wave.get() + 1);
            waveTimer.capture();
        }
    }

    public void spawn(String entityName)
    {
        SpawnData data = new SpawnData(FXGLMath.randomPoint(Constants.GAME_SCENE_RECT));
        data.put("wave", wave.get());
        internalWorld.spawn(entityName, data);
    }

    public static Entity getPlayer()
    {
        return FXGL.getGameWorld()
                .getSingleton(EntityType.PLAYER);
    }
}
