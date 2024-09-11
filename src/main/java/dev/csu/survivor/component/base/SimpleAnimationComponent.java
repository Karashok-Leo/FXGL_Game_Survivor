package dev.csu.survivor.component.base;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

/**
 * 控制实体动画的组件
 * 适用于实体只有一组动画的情况
 * <p>
 * 该组件会自行处理动画纹理以及碰撞箱之间的位置关系，确保实体的中心点位于同时动画纹理和碰撞箱的中心。
 * 同时该组件还将实体显示的Z轴位置绑定至实体在场景中的y轴位置，这是为了自然地实现俯视角的伪3D效果
 * </p>
 * <p>依赖的组件: {@link BoundingBoxComponent} </p>
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
