package dev.csu.survivor.input;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MouseEventHandler implements EventHandler<MouseEvent>
{
    @Override
    public void handle(MouseEvent event)
    {
        switch (event.getButton())
        {
            case PRIMARY ->
            {
//                    FXGL.getGameWorld().spawn("enemy", e.getX(), e.getY());
            }
        }
    }
}
