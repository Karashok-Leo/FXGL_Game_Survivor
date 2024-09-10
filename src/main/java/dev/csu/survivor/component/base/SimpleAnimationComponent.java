package dev.csu.survivor.component.base;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

/**
 * Should be added after BoundingBoxComponent
 */
public class SimpleAnimationComponent extends Component
{
    protected final AnimationChannel channel;
    protected final AnimatedTexture texture;

    public SimpleAnimationComponent(AnimationChannel channel)
    {
        this.channel = channel;
        this.texture = new AnimatedTexture(channel);
    }


    @Override
    public void onAdded()
    {
        this.entity.getViewComponent().addChild(texture);

        BoundingBoxComponent bbox = this.entity.getBoundingBoxComponent();
        this.texture.setTranslateX((bbox.getWidth() - channel.getFrameWidth(0)) / 2.0);
        this.texture.setTranslateY((bbox.getHeight() - channel.getFrameHeight(0)) / 2.0);

        // Bind zIndex to yPos
        this.entity.getViewComponent().zIndexProperty().bind(entity.yProperty());

        this.texture.loop();
        this.entity.setScaleX(2);
        this.entity.setScaleY(2);
    }
}
