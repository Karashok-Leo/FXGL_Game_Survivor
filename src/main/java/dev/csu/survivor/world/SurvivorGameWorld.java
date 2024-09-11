package dev.csu.survivor.world;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.time.LocalTimer;
import dev.csu.survivor.Constants;
import dev.csu.survivor.SurvivorGameApp;
import dev.csu.survivor.enums.EnemyType;
import dev.csu.survivor.ui.SurvivorGameHud;
import dev.csu.survivor.util.MathUtil;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

/**
 * 包装后的游戏世界类
 */
public class SurvivorGameWorld
{
    private final LocalTimer spawnTimer;
    private final LocalTimer waveTimer;
    private final GameWorld internalWorld;
    private final SimpleIntegerProperty wave;
    private final SimpleIntegerProperty enemies;
    private final Entity player;
    private final Entity land;

    public SurvivorGameWorld(GameWorld internalWorld)
    {
        this.spawnTimer = FXGL.newLocalTimer();
        this.waveTimer = FXGL.newLocalTimer();
        this.spawnTimer.capture();
        this.waveTimer.capture();
        this.internalWorld = internalWorld;
        this.wave = new SimpleIntegerProperty(1);
        this.enemies = new SimpleIntegerProperty(0);
        this.player = this.internalWorld.spawn("player", Constants.Common.PLAYER_SPAWN_POINT);
        this.land = this.internalWorld.spawn("land");

        // create random bushes
        for (int i = 0; i < Constants.Common.RANDOM_BUSH_COUNTS; i++)
            this.internalWorld.spawn("bush", FXGLMath.randomPoint(Constants.GAME_SCENE_RECT));
    }

    /**
     * 获取游戏世界对象
     *
     * @return 游戏世界对象
     */
    public static SurvivorGameWorld getInstance()
    {
        return ((SurvivorGameApp) FXGL.getApp()).getWorld();
    }

    /**
     * 获取游戏世界中唯一的玩家
     *
     * @return 游戏世界中唯一的玩家实体
     */
    public static Entity getPlayer()
    {
        return getInstance().player;
    }

    /**
     * 游戏帧方法
     */
    public void tick()
    {
        this.enemies.set(
                this.internalWorld
                        .getEntitiesByType(EnemyType.values())
                        .size()
        );

        if (spawnTimer.elapsed(Constants.Common.ENEMY_SPAWN_DURATION))
        {
            spawnTimer.capture();
            for (int i = 0; i < FXGLMath.random(1, Constants.Common.MAX_ENEMIES_SPAWNED_AT_ONCE); i++)
            {
                EnemyType enemyType = MathUtil.weightedRandom(List.of(EnemyType.values()), type -> type.weight);
                spawn(enemyType.spawnName);
            }
        }

        if (waveTimer.elapsed(Constants.Common.WAVE_CIRCLE))
        {
            wave.set(wave.get() + 1);
            waveTimer.capture();
            SurvivorGameHud.INSTANCE.getShopMenu().refreshShopEntries();
        }
    }

    /**
     * 生成指定实体，并在 SpawnData 中传入当前波次
     *
     * @param entityName 实体 Spawner 名称
     * @see dev.csu.survivor.factory.SurvivorEntityFactory
     */
    public void spawn(String entityName)
    {
        SpawnData data = new SpawnData(FXGLMath.randomPoint(Constants.GAME_SCENE_RECT));
        data.put("wave", wave.get());
        internalWorld.spawn(entityName, data);
    }

    /**
     * 当前波次
     */
    public SimpleIntegerProperty waveProperty()
    {
        return wave;
    }

    /**
     * 当前剩余敌人
     */
    public SimpleIntegerProperty enemiesProperty()
    {
        return enemies;
    }
}
