package dev.csu.survivor.util;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.math.Vec2;
import dev.csu.survivor.enums.Direction;

public class MathUtil
{
    public static Direction getDirectionByVec2(Vec2 v)
    {
        Direction result = Direction.DOWN;
        float angle = 180;
        for (Direction direction : Direction.values())
        {
            float abs = FXGLMath.abs(direction.vector.angle(v));
            if (abs < angle)
            {
                result = direction;
                angle = abs;
            }
        }
        return result;
    }
}
