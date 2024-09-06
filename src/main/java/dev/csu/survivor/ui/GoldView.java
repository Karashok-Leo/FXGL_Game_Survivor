package dev.csu.survivor.ui;

import com.almasb.fxgl.core.Updatable;
import com.almasb.fxgl.texture.AnimatedTexture;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.GoldAnimationComponent;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GoldView extends Parent implements Updatable
{
    protected final AnimatedTexture texture = new AnimatedTexture(GoldAnimationComponent.CHANNEL);
    protected final Label label;

    public GoldView()
    {
        this.texture.setScaleX(2);
        this.texture.setScaleY(2);
        this.texture.loop();

        this.label = new Label();
        this.label.setFont(Font.font(Constants.Client.HUD_FONT));
        this.label.setTextFill(Color.GOLD);
        this.label.translateXProperty().bind(texture.fitWidthProperty().add(16));
        this.label.translateYProperty().bind(texture.fitHeightProperty().divide(2).subtract(label.heightProperty().divide(2)));
        this.getChildren().addAll(texture, label);
    }

    public void bindGolds(ObservableValue<?> golds)
    {
        this.label.textProperty().bind(Bindings.convert(golds));
    }

    @Override
    public void onUpdate(double tpf)
    {
        this.texture.onUpdate(tpf);
    }
}
