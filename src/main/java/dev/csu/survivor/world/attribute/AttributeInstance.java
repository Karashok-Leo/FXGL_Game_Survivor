package dev.csu.survivor.world.attribute;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AttributeInstance {
    private double baseValue;
    private final Map<String, AttributeModifier> modifiers = new HashMap<>();
    private boolean dirty = true;
    private double value;
    private final Consumer<AttributeInstance> updateCallback;

    public AttributeInstance(double baseValue, Consumer<AttributeInstance> updateCallback) {
        this.baseValue = baseValue;
        this.updateCallback = updateCallback;
    }

    public double getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(double baseValue) {
        if (baseValue != this.baseValue) {
            this.baseValue = baseValue;
            onUpdate();
        }
    }

    public void addModifier(String itemId, AttributeModifier modifier) {
        modifiers.put(itemId, modifier);
        dirty = true;
    }

    public void removeModifier(String itemId) {
        modifiers.remove(itemId);
        dirty = true;
    }

    public double getTotalValue() {
        if (dirty) {
            calculateTotalValue();
            dirty = false;
        }
        return value;
    }

    private void calculateTotalValue() {
        double totalValue = baseValue;

        for (AttributeModifier modifier : modifiers.values()) {
            if (modifier.operation() == ModifierOperation.ADDITION) {
                totalValue += modifier.value();
            } else if (modifier.operation() == ModifierOperation.MULTIPLICATION) {
                totalValue *= modifier.value();
            }
        }

        value = totalValue;
        if (updateCallback != null) {
            updateCallback.accept(this);
        }
    }

    private void onUpdate() {
        if (updateCallback != null) {
            updateCallback.accept(this);
        }
    }
}