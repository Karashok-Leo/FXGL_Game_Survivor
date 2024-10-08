package dev.csu.survivor.component.misc.land;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.almasb.fxgl.texture.Texture;
import dev.csu.survivor.Constants;
import javafx.geometry.Point2D;
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
        // create empty grass land
        viewComponent.addChild(createEmptyGrass());
        // create random features (flower...)
        for (int i = 0; i < Constants.Common.RANDOM_FEATURE_COUNTS; i++)
            viewComponent.addChild(createRandomFeature(FXGLMath.randomPoint(Constants.GAME_SCENE_RECT)));
    }

    /**
     * 创建与游戏场景大小相同的一片空草地
     *
     * @return 草地对应的 Rectangle 对象
     */
    public Rectangle createEmptyGrass()
    {
        return new Rectangle(Constants.GAME_SCENE_WIDTH, Constants.GAME_SCENE_HEIGHT, Color.rgb(129, 186, 68));
    }

    public Texture createRandomFeature(Point2D point)
    {
        return createRandomFeature(point.getX(), point.getY());
    }

    /**
     * 在某点位添加 Feature，即花草等点缀
     *
     * @param x X轴位置
     * @param y Y轴位置
     * @return Feature 的 Texture 对象
     */
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
