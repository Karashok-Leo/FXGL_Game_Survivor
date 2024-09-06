package dev.csu.survivor;

import com.almasb.fxgl.app.ApplicationMode;
import dev.csu.survivor.component.AnimationComponent;
import dev.csu.survivor.enums.EntityStates;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;

/**
 * A static class used for constants storage.
 * should be replaced by config files in the future.
 */
public interface Constants
{
    String GAME_NAME = "Survivor";
    String GAME_VERSION = "0.1.0";
    int GAME_SCENE_WIDTH = 1600;
    int GAME_SCENE_HEIGHT = 900;
    Rectangle2D GAME_SCENE_RECT = new Rectangle2D(0, 0, GAME_SCENE_WIDTH, GAME_SCENE_HEIGHT);
    boolean MAIN_MENU_ENABLED = true;
    boolean DEVELOPER_MENU_ENABLED = true;
    ApplicationMode APP_MODE = ApplicationMode.DEVELOPER;

    interface Common
    {
        int PLAYER_INITIAL_MAX_HEALTH = 10;
        double PLAYER_SPEED = 2;
        double PLAYER_HIT_BOX_RADIUS = 7;

        int ENEMY_INITIAL_MAX_HEALTH = 10;
        double ENEMY_MIN_SPEED = 0.6;
        double ENEMY_MAX_SPEED = 1.2;
        double ENEMY_HIT_BOX_RADIUS = 11;

        Point2D PLAYER_SPAWN_POINT = new Point2D(GAME_SCENE_WIDTH / 2.0, GAME_SCENE_HEIGHT / 2.0);
        Duration ENEMY_SPAWN_DURATION = Duration.seconds(4.0);
        Duration ENEMY_IDLE_DURATION = Duration.seconds(0.4);
        Duration ATTACK_DURATION = Duration.seconds(0.8);
        Duration HURT_DELAY = Duration.seconds(0.2);
        Duration HURT_DURATION = Duration.seconds(0.3);
        Duration DEATH_DURATION = Duration.seconds(1.0);
        Duration DEATH_DELAY = Duration.seconds(1.5);
    }

    interface Client
    {
        int PLAYER_HEALTH_BAR_WIDTH = 300;
        int PLAYER_HEALTH_BAR_HEIGHT = 30;
        int HUD_PADDING = 16;
        int HUD_SPACING = 14;
        int HUD_FONT = 30;
        int ENEMY_HEALTH_BAR_WIDTH = 50;
        int ENEMY_HEALTH_BAR_HEIGHT = 10;
    }

    interface AnimationMaps
    {
        AnimationComponent.AnimationMap PLAYER_ANIMATION_MAP = new AnimationComponent.AnimationMap(
                "player",
                new AnimationComponent.StateEntry(EntityStates.IDLE, Duration.seconds(2), new int[]{4, 12, 12, 12}),
                new AnimationComponent.StateEntry(EntityStates.RUN, 0.8, 8),
                new AnimationComponent.StateEntry(EntityStates.HURT, Common.HURT_DURATION, 5),
                new AnimationComponent.StateEntry(EntityStates.DEATH, Common.DEATH_DURATION, 7)
        );

        AnimationComponent.AnimationMap ENEMY_ANIMATION_MAP = new AnimationComponent.AnimationMap(
                "enemy",
                new AnimationComponent.StateEntry(EntityStates.IDLE, Common.ENEMY_IDLE_DURATION, 4),
                new AnimationComponent.StateEntry(EntityStates.RUN, 0.8, 8),
                new AnimationComponent.StateEntry(EntityStates.ATTACK, Common.ATTACK_DURATION, 8),
                new AnimationComponent.StateEntry(EntityStates.HURT, Common.HURT_DURATION, 6),
                new AnimationComponent.StateEntry(EntityStates.DEATH, Common.DEATH_DURATION, 8)
        );
    }
}
