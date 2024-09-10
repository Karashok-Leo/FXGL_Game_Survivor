package dev.csu.survivor.enums;

import java.util.Locale;

public enum EnemyType
{
    MELEE_ENEMY(2),
    RANGED_ENEMY(1),
    ;
    public final int weight;
    public final String spawnName;

    EnemyType(int weight)
    {
        this.weight = weight;
        this.spawnName = name().toLowerCase(Locale.ROOT);
    }
}
