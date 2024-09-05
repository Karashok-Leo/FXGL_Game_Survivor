package dev.csu.survivor.input;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import dev.csu.survivor.enums.Direction;
import dev.csu.survivor.input.action.MoveAction;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class SurvivorGameInput
{
    public void init(Input input)
    {
        input.addAction(new MoveAction(Direction.UP), KeyCode.W);
        input.addAction(new MoveAction(Direction.DOWN), KeyCode.S);
        input.addAction(new MoveAction(Direction.LEFT), KeyCode.A);
        input.addAction(new MoveAction(Direction.RIGHT), KeyCode.D);
        input.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
        {
            switch (e.getButton())
            {
                case PRIMARY ->
                {
                    FXGL.getGameWorld().spawn("enemy", e.getX(), e.getY());
                }
            }
        });
    }
}
