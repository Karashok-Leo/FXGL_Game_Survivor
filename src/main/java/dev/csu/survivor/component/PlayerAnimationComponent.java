package dev.csu.survivor.component;

import dev.csu.survivor.enums.EntityStates;

public class PlayerAnimationComponent extends AnimationComponent
{
    public PlayerAnimationComponent()
    {
        super(
                new AnimationMap(
                        "player",
                        new StateEntry(EntityStates.IDLE, 2, new int[]{4, 12, 12, 12}),
                        new StateEntry(EntityStates.RUN, 0.8, 8),
                        new StateEntry(EntityStates.HURT, 0.5, 5),
                        new StateEntry(EntityStates.DEATH, 0.9, 7)
                )
        );
    }
}
