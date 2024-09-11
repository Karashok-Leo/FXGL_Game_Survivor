package dev.csu.survivor.enums;

import com.almasb.fxgl.entity.state.EntityState;

/**
 * 实体状态种类
 */
public class EntityStates
{
    /**
     * 站立
     */
    public static final EntityState IDLE = new EntityState("idle");

    /**
     * 奔跑
     */
    public static final EntityState RUN = new EntityState("run");

    /**
     * 攻击
     */
    public static final EntityState ATTACK = new EntityState("attack");

    /**
     * 受伤
     */
    public static final EntityState HURT = new EntityState("hurt");

    /**
     * 死亡
     */
    public static final EntityState DEATH = new EntityState("death");
}
