package dev.csu.survivor.component.misc.land;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.image.Image;

public class RandomBushComponent extends Component
{
    public static final Image[] images = new Image[40];

    static
    {
        for (int i = 0; i < images.length; i++)
            images[i] = FXGL.image("bush/bush%d.png".formatted(i));
    }

    public RandomBushComponent()
    {
    }

    @Override
    public void onAdded()
    {
        ViewComponent viewComponent = this.entity.getViewComponent();
        // Bind zIndex to yPos
        viewComponent.zIndexProperty().bind(entity.yProperty());
        viewComponent.addChild(createRandomBush());
        this.entity.setScaleX(2);
        this.entity.setScaleY(2);
    }

    protected Texture createRandomBush()
    {
        int random = FXGLMath.random(0, images.length - 1);
        return new Texture(images[random]);
    }
}
