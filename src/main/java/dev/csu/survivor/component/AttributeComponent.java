package dev.csu.survivor.component;

import com.almasb.fxgl.entity.component.Component;
import java.util.HashMap;
import java.util.Map;

public class AttributeComponent extends Component
{

    private double baseSpeedMultiplier = 1.0;
    private int baseHealthAddition = 0;

    private final Map<String, Double> speedModifiers = new HashMap<>();
    private final Map<String, Integer> healthModifiers = new HashMap<>();

    public double getBaseSpeedMultiplier() {
        return baseSpeedMultiplier;
    }

    public void setBaseSpeedMultiplier(double baseSpeedMultiplier) {
        this.baseSpeedMultiplier = baseSpeedMultiplier;
    }

    public int getBaseHealthAddition() {
        return baseHealthAddition;
    }

    public void setBaseHealthAddition(int baseHealthAddition) {
        this.baseHealthAddition = baseHealthAddition;
    }

    public void addSpeedModifier(String itemId, double modifier) {
        speedModifiers.put(itemId, modifier);
    }

    public void removeSpeedModifier(String itemId) {
        speedModifiers.remove(itemId);
    }

    public double calculateTotalSpeedMultiplier() {
        double totalMultiplier = baseSpeedMultiplier;
        for (double modifier : speedModifiers.values()) {
            totalMultiplier *= modifier;
        }
        return totalMultiplier;
    }

    public void addHealthModifier(String itemId, int modifier) {
        healthModifiers.put(itemId, modifier);
    }

    public void removeHealthModifier(String itemId) {
        healthModifiers.remove(itemId);
    }

    public int calculateTotalHealthAddition() {
        int totalHealth = baseHealthAddition;
        for (int modifier : healthModifiers.values()) {
            totalHealth += modifier;
        }
        return totalHealth;
    }

}
