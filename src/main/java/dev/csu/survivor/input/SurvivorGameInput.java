package dev.csu.survivor.input;

import com.almasb.fxgl.input.Input;
import dev.csu.survivor.enums.Direction;
import dev.csu.survivor.input.action.MoveAction;
import dev.csu.survivor.input.action.OpenShopAction;
import dev.csu.survivor.input.action.ShootBulletAction;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

/**
 * 所有控制交互的初始化位置
 */
public class SurvivorGameInput
{
    public void init(Input input)
    {
        input.addAction(new MoveAction(Direction.UP), KeyCode.W);
        input.addAction(new MoveAction(Direction.DOWN), KeyCode.S);
        input.addAction(new MoveAction(Direction.LEFT), KeyCode.A);
        input.addAction(new MoveAction(Direction.RIGHT), KeyCode.D);
        input.addAction(new OpenShopAction(), KeyCode.TAB);
        input.addAction(new ShootBulletAction(), MouseButton.PRIMARY);
    }
}
