package dev.csu.survivor.component;

import com.almasb.fxgl.dsl.components.RechargeableIntComponent;
import com.almasb.fxgl.entity.Entity;
import dev.csu.survivor.Constants;

public class GoldComponent extends RechargeableIntComponent {


    public GoldComponent() {
        super(Constants.Common.MAX_GOLDS, Constants.Common.INITIAL_GOLDS);
    }

    public void collect(Entity gold){

        gold.removeFromWorld();

        setValue(getValue() + 1);
    }
}

