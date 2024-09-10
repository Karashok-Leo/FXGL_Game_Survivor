package dev.csu.survivor.component.misc;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.almasb.fxgl.texture.Texture;
import dev.csu.survivor.Constants;

public class BoomerangSelfComponent extends Component
{
    public static final Texture TEXTURE = FXGL.texture("item/boomerang_attack.png");

    @Override
    public void onAdded()
    {
        Texture texture = TEXTURE.copy();

        BoundingBoxComponent bbox = this.entity.getBoundingBoxComponent();
        texture.setTranslateX((bbox.getWidth() - texture.getWidth()) / 2.0);
        texture.setTranslateY((bbox.getHeight() - texture.getHeight()) / 2.0);

        ViewComponent view = this.entity.getViewComponent();
        view.addChild(texture);
        // Bind zIndex to yPos
        view.zIndexProperty().bind(entity.yProperty());

        this.entity.setScaleX(2);
        this.entity.setScaleY(2);
    }

    @Override
    public void onUpdate(double tpf)
    {
        entity.rotateBy(tpf * Constants.Common.BOOMERANG_ROTATION_SPEED);
    }
}
