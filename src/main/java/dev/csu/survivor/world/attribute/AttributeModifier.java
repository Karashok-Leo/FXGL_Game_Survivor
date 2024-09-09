package dev.csu.survivor.world.attribute;

import java.util.UUID;

public record AttributeModifier(
        UUID uuid,
        String className,
        Operation operation,
        double value
)
{
    public AttributeModifier(
            String className,
            Operation operation,
            double value
    )
    {
        this(UUID.randomUUID(), className, operation, value);
    }

    public enum Operation
    {
        ADDITION("    + %.2f"),
        MULTIPLICATION("    × %.2f"),
        ;
        private final String formatter;

        Operation(String formatter)
        {
            this.formatter = formatter;
        }

        public String formatModifier(double value)
        {
            return formatter.formatted(value);
        }
    }
}
