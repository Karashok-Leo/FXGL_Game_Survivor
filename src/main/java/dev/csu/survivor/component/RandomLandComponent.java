package dev.csu.survivor.component;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.almasb.fxgl.texture.Texture;
import dev.csu.survivor.Constants;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RandomLandComponent extends Component
{
    public static final Image[] images = new Image[4];

    static
    {
        for (int i = 0; i < images.length; i++)
            images[i] = FXGL.image("land/land%d.png".formatted(i));
    }

    public RandomLandComponent()
    {
    }

    @Override
    public void onAdded()
    {
        ViewComponent viewComponent = this.entity.getViewComponent();
        viewComponent.setZIndex(-99);
        viewComponent.addChild(createEmptyGrass());
        for (int x = 0; x < Constants.GAME_SCENE_WIDTH; x += 48)
            for (int y = 0; y < Constants.GAME_SCENE_HEIGHT; y += 48)
                if (FXGLMath.randomBoolean(0.4))
                    viewComponent.addChild(createRandomFeature(x, y));
    }

    public Rectangle createEmptyGrass()
    {
        return new Rectangle(Constants.GAME_SCENE_WIDTH, Constants.GAME_SCENE_HEIGHT, Color.rgb(129, 186, 68));
    }

    public Texture createRandomFeature(double x, double y)
    {
        int random = FXGLMath.random(0, images.length - 1);
        Texture texture = new Texture(images[random]);
        texture.setX(x);
        texture.setY(y);
        texture.setScaleX(2);
        texture.setScaleY(2);
        return texture;
    }
}
