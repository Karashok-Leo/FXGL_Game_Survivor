package dev.csu.survivor.enums;

import com.almasb.fxgl.entity.state.EntityState;

public class EntityStates
{
    public static final EntityState IDLE = new EntityState("idle");
    public static final EntityState RUN = new EntityState("run");
    public static final EntityState ATTACK = new EntityState("attack");
    public static final EntityState HURT = new EntityState("hurt");
    public static final EntityState DEATH = new EntityState("death");
}
