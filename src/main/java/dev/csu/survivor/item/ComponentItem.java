package dev.csu.survivor.item;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.EntityType;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public record ComponentItem(EntityType type) implements Item
{
    @Override
    public void onApply(Entity entity)
    {
        FXGL.getGameWorld().spawn("boomerang");
    }

    @Override
    public List<Text> getDescription(Entity entity)
    {
        List<Text> tooltip = new ArrayList<>();

        Text componentName  = new Text("Boomerang:");
        componentName.setFill(Color.WHITE);
        Text modifierText = new Text("    + 1");
        modifierText.setFill(Color.WHITE);
        tooltip.add(componentName);
        tooltip.add(modifierText);

        return tooltip;
    }
}
