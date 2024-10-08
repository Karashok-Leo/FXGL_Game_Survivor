package dev.csu.survivor;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.AnimationChannel;
import dev.csu.survivor.component.base.MultiAnimationComponent;
import dev.csu.survivor.enums.EntityStates;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * 用于存储常量，未来应该转化为配置文件。
 * A static class used for constants storage.
 * Should be replaced by config files in the future.
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
        double PLAYER_INITIAL_MAX_HEALTH = 10;
        double PLAYER_INITIAL_SPEED = 2;
        double PLAYER_INITIAL_DAMAGE = 2;
        double PLAYER_HIT_BOX_RADIUS = 7;
        double PLAYER_BULLET_SPEED = 600;
        Duration PLAYER_SHOOT_COOLDOWN = Duration.seconds(0.4);

        double ENEMY_INITIAL_MAX_HEALTH = 5;
        double ENEMY_INITIAL_MIN_SPEED = 0.4;
        double ENEMY_INITIAL_MAX_SPEED = 0.8;
        double ENEMY_INITIAL_DAMAGE = 1;
        double ENEMY_HIT_BOX_RADIUS = 12;

        double RANGED_ENEMY_ATTACK_RANGE = 300;
        double RANGED_ENEMY_BULLET_SPEED = 300;

        Point2D PLAYER_SPAWN_POINT = new Point2D(GAME_SCENE_WIDTH / 2.0, GAME_SCENE_HEIGHT / 2.0);
        Duration ENEMY_SPAWN_DURATION = Duration.seconds(4.0);
        int MAX_ENEMIES_SPAWNED_AT_ONCE = 4;

        Duration ENEMY_IDLE_DURATION = Duration.seconds(0.4);
        Duration ATTACK_DURATION = Duration.seconds(0.8);
        Duration RANGED_ENEMY_ATTACK_COOLDOWN = Duration.seconds(2.4);
        Duration HURT_DELAY = Duration.seconds(0.2);
        Duration HURT_COOLDOWN = Duration.seconds(0.8);
        Duration HURT_DURATION = Duration.seconds(0.3);
        Duration DEATH_DURATION = Duration.seconds(1.0);
        Duration DEATH_DELAY = Duration.seconds(1.5);

        double GOLD_HIT_BOX_RADIUS = 12;
        int INITIAL_GOLDS = 20;
        int MAX_GOLDS = 999;

        double HEALTH_CRYSTAL_VALUE = 5;
        double HEALING_POTION_VALUE = 0.2;
        double ACCELERATE_FEATHER_DEGREE = 1.2;
        double POWER_CRYSTAL_VALUE = 1;

        int RANDOM_FEATURE_COUNTS = 96;
        int RANDOM_BUSH_COUNTS = 32;

        double BOOMERANG_ROTATION_SPEED = 600;
        double BOOMERANG_REVOLUTION_SPEED = 4;
        double BOOMERANG_REVOLUTION_RADIUS = 100;
        double BOOMERANG_HIT_BOX_RADIUS = 10;

        Duration WAVE_CIRCLE = Duration.minutes(2);
    }

    interface Client
    {
        int PLAYER_HEALTH_BAR_WIDTH = 300;
        int PLAYER_HEALTH_BAR_HEIGHT = 30;
        int HUD_PADDING = 16;
        int HUD_SPACING = 14;
        int ENEMY_HEALTH_BAR_WIDTH = 50;
        int ENEMY_HEALTH_BAR_HEIGHT = 10;

        int SHOP_ENTRY_WIDTH = 200;
        int SHOP_ENTRY_HEIGHT = 480;
        int SHOP_ENTRY_PADDING = 20;
        int SHOP_ENTRY_OUTER_SPACING = 0;
        int SHOP_ENTRY_INNER_SPACING = 30;
        double SHOP_ITEM_TEXTURE_SCALE = 3.6;
        int SHOP_ITEM_BORDER_SIZE = 160;
        int SHOP_ITEM_NAME_FONT = 20;
        int SHOP_ITEM_DESC_FONT = 14;

        int INVENTORY_COLS = 5;
        int INVENTORY_ROWS = 5;
        int INVENTORY_BORDER_SIZE = 80;
        int INVENTORY_ENTRY_SPACING = 20;
        double INVENTORY_ITEM_TEXTURE_SCALE = 1.8;

        Duration SHOP_ENTRY_FADE_DURATION = Duration.seconds(0.5);
        Duration TEXT_FADE_DURATION = Duration.seconds(1.0);
    }

    interface AnimationMaps
    {
        MultiAnimationComponent.AnimationMap PLAYER_ANIMATION_MAP = new MultiAnimationComponent.AnimationMap(
                "player",
                new MultiAnimationComponent.StateEntry(EntityStates.IDLE, Duration.seconds(2), new int[]{4, 12, 12, 12}),
                new MultiAnimationComponent.StateEntry(EntityStates.RUN, 0.8, 8),
                new MultiAnimationComponent.StateEntry(EntityStates.HURT, Common.HURT_DURATION, 5),
                new MultiAnimationComponent.StateEntry(EntityStates.DEATH, Common.DEATH_DURATION, 7)
        );

        MultiAnimationComponent.AnimationMap MELEE_ENEMY_ANIMATION_MAP = new MultiAnimationComponent.AnimationMap(
                "melee_enemy",
                new MultiAnimationComponent.StateEntry(EntityStates.IDLE, Common.ENEMY_IDLE_DURATION, 4),
                new MultiAnimationComponent.StateEntry(EntityStates.RUN, 0.8, 8),
                new MultiAnimationComponent.StateEntry(EntityStates.ATTACK, Common.ATTACK_DURATION, 8),
                new MultiAnimationComponent.StateEntry(EntityStates.HURT, Common.HURT_DURATION, 6),
                new MultiAnimationComponent.StateEntry(EntityStates.DEATH, Common.DEATH_DURATION, 8)
        );

        MultiAnimationComponent.AnimationMap RANGED_ENEMY_ANIMATION_MAP = new MultiAnimationComponent.AnimationMap(
                "ranged_enemy",
                new MultiAnimationComponent.StateEntry(EntityStates.IDLE, Common.ENEMY_IDLE_DURATION, 4),
                new MultiAnimationComponent.StateEntry(EntityStates.RUN, 0.6, 6),
                new MultiAnimationComponent.StateEntry(EntityStates.ATTACK, Common.ATTACK_DURATION, 8),
                new MultiAnimationComponent.StateEntry(EntityStates.HURT, Common.HURT_DURATION, 6),
                new MultiAnimationComponent.StateEntry(EntityStates.DEATH, Common.DEATH_DURATION, 8)
        );

        AnimationChannel COIN = new AnimationChannel(
                FXGL.image("coin.png"), 9, 20, 20,
                Duration.seconds(2), 0, 7
        );

        AnimationChannel ENEMY_BULLET = new AnimationChannel(
                FXGL.image("sword_fx.png"),
                Duration.seconds(1), 8
        );
    }

    interface Style
    {
        double FONT_SIZE = 24;
        Color FONT_COLOR = Color.WHITESMOKE;
        String FONT_OTHER_STYLE = "-fx-effect: dropshadow(gaussian, black, 3, 0, 0, 3);";

        double SHADOW_GAUSSIAN_RADIUS = 3;
        double SHADOW_GLOW_RADIUS = 3;
    }
}
