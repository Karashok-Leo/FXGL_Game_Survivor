package dev.csu.survivor.enums;

import com.almasb.fxgl.core.math.Vec2;
import dev.csu.survivor.util.StringUtil;

public enum Direction
{
    UP(new Vec2(0, -1)),
    DOWN(new Vec2(0, 1)),
    LEFT(new Vec2(-1, 0)),
    RIGHT(new Vec2(1, 0));

    public final Vec2 vector;
    public final String name;

    Direction(Vec2 vector)
    {
        this.vector = vector;
        this.name = StringUtil.lowercase(this.name());
    }
}
