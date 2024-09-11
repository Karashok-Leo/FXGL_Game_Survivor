package dev.csu.survivor.item;

import com.almasb.fxgl.entity.Entity;
import javafx.scene.text.Text;

import java.util.List;

/**
 * 物品总接口
 */
public interface Item
{
    /**
     * 该物品被添加至实体物品栏时调用
     *
     * @param entity 实体
     */
    default void onApply(Entity entity)
    {
    }

    /**
     * 该物品从实体物品栏移除时调用
     *
     * @param entity 实体
     */
    default void onRemove(Entity entity)
    {
    }

    /**
     * 物品的描述信息<br>
     * 商店以及物品栏界面展示物品详细信息时调用
     */
    default List<Text> getDescription(Entity entity)
    {
        return List.of();
    }
}
