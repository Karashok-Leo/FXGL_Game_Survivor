package dev.csu.survivor.component.player;

import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.component.base.HurtComponent;
import dev.csu.survivor.ui.menu.GameOverMenu;

public class PlayerHurtComponent extends HurtComponent
{
    @Override
    public void onDeath()
    {
        FXGL.getSceneService().pushSubScene(new GameOverMenu());
    }
}
