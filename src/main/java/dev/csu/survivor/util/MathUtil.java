package dev.csu.survivor.util;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.math.Vec2;
import dev.csu.survivor.enums.Direction;

public class MathUtil
{
    public static final double SQRT2 = FXGLMath.sqrt(2) / 2;

    public static Direction getDirectionByVec2(Vec2 v)
    {
        Direction direction = Direction.DOWN;
        Vec2 normalize = v.normalize();
        if (normalize.x < -SQRT2) direction = Direction.LEFT;
        else if (normalize.x > SQRT2) direction = Direction.RIGHT;
        else if (normalize.y < -SQRT2) direction = Direction.UP;
        return direction;
    }
}
