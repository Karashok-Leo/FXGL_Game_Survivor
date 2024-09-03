package dev.csu.survivor.world;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

public class SurvivorGameWorld
{
    private boolean active = false;

    private Entity player;

    public SurvivorGameWorld()
    {
        player = FXGL.getGameWorld().spawn("player", 500, 400);
    }

    public void tick()
    {

    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public Entity getPlayer()
    {
        return player;
    }
}
