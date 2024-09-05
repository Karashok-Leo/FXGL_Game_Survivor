package dev.csu.survivor.input.action;

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
        // NYI
        FXGL.getGameWorld().spawn("enemy");
    }
}
