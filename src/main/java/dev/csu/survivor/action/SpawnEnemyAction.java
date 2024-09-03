package dev.csu.survivor.action;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import org.jetbrains.annotations.NotNull;

public class SpawnEnemyAction extends UserAction
{
    public SpawnEnemyAction(@NotNull String name)
    {
        super(name);
    }

    @Override
    protected void onActionEnd()
    {

        FXGL.getGameWorld().spawn("enemy");
    }
}
