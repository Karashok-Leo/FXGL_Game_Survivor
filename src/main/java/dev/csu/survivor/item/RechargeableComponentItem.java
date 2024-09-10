package dev.csu.survivor.item;

import com.almasb.fxgl.dsl.components.RechargeableIntComponent;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record RechargeableComponentItem<C extends RechargeableIntComponent>(
        Class<C> clazz,
        Supplier<C> componentFactory,
        String descHead
) implements IRechargeableComponentItem<C>
{
    @Override
    public C getComponent()
    {
        return componentFactory.get();
    }

    @Override
    public Class<C> getComponentClass()
    {
        return clazz;
    }

    @Override
    public List<Text> getDescription(Entity entity)
    {
        List<Text> tooltip = new ArrayList<>();

        Text componentName = new Text(descHead + ":");
        componentName.setFill(Color.WHITE);
        Text modifierText = new Text("    + 1");
        modifierText.setFill(Color.WHITE);
        tooltip.add(componentName);
        tooltip.add(modifierText);

        return tooltip;
    }
}
