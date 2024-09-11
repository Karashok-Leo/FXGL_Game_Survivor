package dev.csu.survivor.enums;

import com.almasb.fxgl.core.math.Vec2;
import dev.csu.survivor.util.StringUtil;

/**
 * 方向枚举
 */
public enum Direction
{
    /**
     * 上
     */
    UP(new Vec2(0, -1)),

    /**
     * 下
     */
    DOWN(new Vec2(0, 1)),

    /**
     * 左
     */
    LEFT(new Vec2(-1, 0)),

    /**
     * 右
     */
    RIGHT(new Vec2(1, 0));

    /**
     * 该方向的单位向量
     */
    public final Vec2 vector;

    /**
     * 该方向的小写名称
     */
    public final String name;

    Direction(Vec2 vector)
    {
        this.vector = vector;
        this.name = StringUtil.lowercase(this.name());
    }
}
