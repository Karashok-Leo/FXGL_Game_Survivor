package dev.csu.survivor.util;

import com.almasb.fxgl.core.math.FXGLMath;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

public class MathUtil
{
    /**
     * 加权随机选择
     *
     * @param pool         选择的池
     * @param weightGetter 权重获取方法
     * @return 随机选择到的对象
     */
    public static <T> T weightedRandom(Collection<T> pool, Function<T, Integer> weightGetter)
    {
        Random random = FXGLMath.getRandom();

        int totalWeight = 0;
        for (T t : pool)
            totalWeight += weightGetter.apply(t);

        int size = pool.size();

        if (totalWeight <= 0 || size == 0) throw new AssertionError();

        int randomInt = random.nextInt(totalWeight);
        for (T t : pool)
        {
            randomInt -= weightGetter.apply(t);
            if (randomInt < 0)
            {
                return t;
            }
        }
        return pool.iterator().next();
    }

    /**
     * 加权随机选择
     *
     * @param n            随机选择的目标数量
     * @param pool         选择的池
     * @param weightGetter 权重获取方法
     * @return 随机选择到的对象集（不含相同对象）
     */
    public static <T> Set<T> weightedRandomSet(int n, Collection<T> pool, Function<T, Integer> weightGetter)
    {
        Random random = FXGLMath.getRandom();

        int totalWeight = 0;
        for (T t : pool)
            totalWeight += weightGetter.apply(t);

        int size = pool.size();

        if (totalWeight <= 0 || size <= n) throw new IllegalArgumentException();

        Set<T> result = new HashSet<>();
        while (result.size() < n)
        {
            int randomInt = random.nextInt(totalWeight);
            for (T t : pool)
            {
                randomInt -= weightGetter.apply(t);
                if (randomInt < 0)
                {
                    result.add(t);
                    break;
                }
            }
        }

        return result;
    }
}
