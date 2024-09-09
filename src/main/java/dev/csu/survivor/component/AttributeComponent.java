package dev.csu.survivor.component;

import com.almasb.fxgl.entity.component.Component;
import dev.csu.survivor.Constants;
import dev.csu.survivor.enums.AttributeType;
import dev.csu.survivor.world.attribute.AttributeInstance;
import dev.csu.survivor.world.attribute.AttributeModifier;

import java.util.EnumMap;
import java.util.Map;

public class AttributeComponent extends Component {

    private final Map<AttributeType, AttributeInstance> attributes = new EnumMap<>(AttributeType.class);

    public AttributeComponent() {
        attributes.put(AttributeType.SPEED, new AttributeInstance(Constants.Common.PLAYER_SPEED, this::onAttributeUpdate));
        attributes.put(AttributeType.HEALTH, new AttributeInstance(0, this::onAttributeUpdate));
    }

    public AttributeInstance getAttributeInstance(AttributeType type) {
        return attributes.get(type);
    }

    public void addModifier(AttributeType type, String itemId, AttributeModifier modifier) {
        AttributeInstance instance = attributes.get(type);
        if (instance != null) {
            instance.addModifier(itemId, modifier);
        }
    }

    public void removeModifier(AttributeType type, String itemId) {
        AttributeInstance instance = attributes.get(type);
        if (instance != null) {
            instance.removeModifier(itemId);
        }
    }

    public double calculateTotalAttribute(AttributeType type) {
        AttributeInstance instance = attributes.get(type);
        return instance != null ? instance.getTotalValue() : 0;
    }

    private void onAttributeUpdate(AttributeInstance instance) {
    }
}




