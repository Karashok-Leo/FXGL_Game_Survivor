package dev.csu.survivor.ui;

import com.almasb.fxgl.core.Updatable;
import com.almasb.fxgl.texture.AnimatedTexture;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.player.GoldComponent;
import dev.csu.survivor.util.StyleUtil;
import dev.csu.survivor.world.SurvivorGameWorld;
import javafx.beans.binding.Bindings;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/**
 * 显示玩家持有金币数量的视图
 */
public class GoldView extends Parent implements Updatable
{
    protected final AnimatedTexture texture = new AnimatedTexture(Constants.AnimationMaps.COIN);
    protected final Label label;

    public GoldView()
    {
        this.texture.setScaleX(2);
        this.texture.setScaleY(2);
        this.texture.loop();

        this.label = new Label();
        this.label.textProperty().bind(
                Bindings.convert(
                        SurvivorGameWorld.getPlayer()
                                .getComponent(GoldComponent.class)
                                .valueProperty()
                )
        );
        StyleUtil.setLabelStyle(this.label, Color.GOLD.brighter());
        this.label.translateXProperty().bind(texture.fitWidthProperty().add(16));
        this.label.translateYProperty().bind(texture.fitHeightProperty().divide(2).subtract(label.heightProperty().divide(2)));
        this.getChildren().addAll(texture, label);
    }

    public Label getLabel()
    {
        return label;
    }

    @Override
    public void onUpdate(double tpf)
    {
        this.texture.onUpdate(tpf);
    }
}
