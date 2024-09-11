package dev.csu.survivor.world.attribute;

import java.util.UUID;

/**
 * 属性修饰符
 *
 * @param uuid      每一个属性修饰符都拥有唯一的 UUID
 * @param className 类别名称。不同属性修饰符的类别名称可以相同
 * @param operation 决定属性修饰符的运算方法
 * @param value     属性修饰符的值
 */
public record AttributeModifier(
        UUID uuid,
        String className,
        Operation operation,
        double value
)
{
    /**
     * 创建一个随机 UUID 的属性修饰符
     *
     * @param className 类别名称
     * @param operation 运算符
     * @param value     值
     */
    public AttributeModifier(
            String className,
            Operation operation,
            double value
    )
    {
        this(UUID.randomUUID(), className, operation, value);
    }

    /**
     * 运算符
     */
    public enum Operation
    {
        /**
         * 加算
         */
        ADDITION("    + %.2f"),

        /**
         * 乘算
         * <p>作用于加算之后</p>
         */
        MULTIPLICATION("    × %.2f"),
        ;
        private final String formatter;

        Operation(String formatter)
        {
            this.formatter = formatter;
        }

        /**
         * 获取格式化描述字符串
         * <p>在获取 <code>AttributeItem</code> 的物品描述信息时调用</p>
         *
         * @param value 修饰符的值
         * @return 格式化描述字符串
         * @see dev.csu.survivor.item.AttributeItem
         */
        public String formatModifier(double value)
        {
            return formatter.formatted(value);
        }
    }
}
