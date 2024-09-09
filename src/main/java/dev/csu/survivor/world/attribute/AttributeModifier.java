package dev.csu.survivor.world.attribute;

public record AttributeModifier(
        String className,
        Operation operation,
        double value
)
{
    public enum Operation
    {
        ADDITION,
        MULTIPLICATION
    }
}
