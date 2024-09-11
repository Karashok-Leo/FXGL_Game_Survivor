package dev.csu.survivor.enums;

import java.util.Locale;

/**
 * 敌人实体种类
 */
public enum EnemyType
{
    /**
     * 近战敌人
     */
    MELEE_ENEMY(2),

    /**
     * 远程敌人
     */
    RANGED_ENEMY(1),
    ;

    /**
     * <p>该类敌人的权重</p>
     * <p>该值越大，游戏场景内刷新敌人时越有可能出现该类敌人</p>
     */
    public final int weight;

    /**
     * 该类敌人对应的 Spawner 名称
     *
     * @see dev.csu.survivor.factory.SurvivorEntityFactory
     */
    public final String spawnName;

    EnemyType(int weight)
    {
        this.weight = weight;
        this.spawnName = name().toLowerCase(Locale.ROOT);
    }
}
