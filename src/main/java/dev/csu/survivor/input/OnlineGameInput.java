package dev.csu.survivor.input;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.net.Connection;
import dev.csu.survivor.input.action.SpawnEnemyAction;
import javafx.scene.input.MouseButton;

public class OnlineGameInput extends SurvivorGameInput
{
    private Connection<Bundle> connection;

    public void initServerInput(Input input, Connection<Bundle> connection)
    {
        init(input);
    }

    public void initClientInput(Input input, Connection<Bundle> connection)
    {
        input.addAction(new SpawnEnemyAction(connection), MouseButton.PRIMARY);
    }
}
