package dev.csu.survivor.component.misc;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.util.Duration;

public class EnemyBulletAnimationComponent extends Component
{
    public static final AnimationChannel CHANNEL = new AnimationChannel(
            FXGL.image("sword_fx.png"),
            Duration.seconds(1), 8
    );

    protected final AnimatedTexture texture = new AnimatedTexture(CHANNEL);

    @Override
    public void onAdded()
    {
        this.entity.getViewComponent().addChild(texture);

        BoundingBoxComponent bbox = this.entity.getBoundingBoxComponent();
        this.texture.setTranslateX((bbox.getWidth() - CHANNEL.getFrameWidth(0)) / 2.0);
        this.texture.setTranslateY((bbox.getHeight() - CHANNEL.getFrameHeight(0)) / 2.0);

        // Bind zIndex to yPos
        this.entity.getViewComponent().zIndexProperty().bind(entity.yProperty());

        this.texture.loop();
        this.entity.setScaleX(2);
        this.entity.setScaleY(2);
    }
}
