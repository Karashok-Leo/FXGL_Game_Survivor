package dev.csu.survivor.component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.physics.box2d.collision.Distance;
import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.EntityStates;
import dev.csu.survivor.world.SurvivorGameWorld;
import javafx.geometry.Point2D;

public class RangedEnemyComponent extends Component {
    protected StateComponent state;
    protected MotionComponent motion;

    public RangedEnemyComponent()
    {
    }

    @Override
    public void onAdded()
    {
        state = entity.getComponent(StateComponent.class);
        motion = entity.getComponent(MotionComponent.class);
    }

    @Override
    public void onUpdate(double tpf)
    {
//        if (state.isIn(EntityStates.IDLE))
//            state.changeState(EntityStates.RUN);
//        if (state.isIn(EntityStates.RUN, EntityStates.HURT))
//        {
//            Point2D target = SurvivorGameWorld
//                    .getPlayer()
//                    .getPosition();
//            Point2D subtract = target.subtract(entity.getPosition());
//            motion.addVelocity(subtract.getX(), subtract.getY());
//        }
        Point2D target = SurvivorGameWorld.getPlayer().getPosition();
        double distance = entity.distance(SurvivorGameWorld.getPlayer());

        if (state.isIn(EntityStates.IDLE))
            state.changeState(EntityStates.RUN);
        if(state.isIn(EntityStates.RUN , EntityStates.HURT)){
            if( distance > Constants.Common.RANGED_ENEMY_ATTACK_RANGE ){
                Point2D subtract = target.subtract(entity.getPosition());
                motion.addVelocity(subtract.getX(),subtract.getY());
            }
            if (distance < Constants.Common.RANGED_ENEMY_ATTACK_RANGE){
                state.changeState(EntityStates.ATTACK);
            }
        }
    }
}
