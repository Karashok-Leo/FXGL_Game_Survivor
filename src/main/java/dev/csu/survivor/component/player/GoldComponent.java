package dev.csu.survivor.component.player;

import com.almasb.fxgl.dsl.components.RechargeableIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.AnimatedTexture;
import dev.csu.survivor.Constants;
import dev.csu.survivor.ui.SurvivorGameHud;

public class GoldComponent extends RechargeableIntComponent
{
    public GoldComponent()
    {
        super(Constants.Common.MAX_GOLDS, Constants.Common.INITIAL_GOLDS);
    }

    public void collect(Entity gold)
    {
        gold.removeFromWorld();

        AnimatedTexture texture = new AnimatedTexture(Constants.AnimationMaps.COIN);
        texture.setX(gold.getX());
        texture.setY(gold.getY());
        texture.setScaleX(2);
        texture.setScaleY(2);
        SurvivorGameHud.INSTANCE.createGoldCollectingAnimation(texture, gold.getX(), gold.getY());

        restore(1);
    }
}

