package dev.csu.survivor.component.enemy;

import com.almasb.fxgl.entity.Entity;
import dev.csu.survivor.component.base.AttackComponent;
import dev.csu.survivor.component.base.AttributeComponent;
import dev.csu.survivor.component.base.HurtComponent;
import dev.csu.survivor.enums.AttributeType;
import dev.csu.survivor.util.ComponentUtil;

public class MeleeAttackComponent extends AttackComponent
{
    @Override
    protected void doAttack(Entity target)
    {
        ComponentUtil.findAndConsumeComponentByClass(
                target,
                HurtComponent.class,
                hurtComponent -> hurtComponent.hurt(
                        entity,
                        entity.getComponent(AttributeComponent.class)
                                .getAttributeValue(AttributeType.DAMAGE)
                )
        );
    }
}
