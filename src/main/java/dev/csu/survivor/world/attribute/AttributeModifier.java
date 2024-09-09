package dev.csu.survivor.world.attribute;

public record AttributeModifier(
        String className,
        Operation operation,
        double value
)
{
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
