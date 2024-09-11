package dev.csu.survivor.enums;

import com.almasb.fxgl.core.util.LazyValue;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import dev.csu.survivor.Constants;
import dev.csu.survivor.component.player.BoomerangComponent;
import dev.csu.survivor.item.AttributeItem;
import dev.csu.survivor.item.Item;
import dev.csu.survivor.item.RechargeableComponentItem;
import dev.csu.survivor.util.StringUtil;
import dev.csu.survivor.world.attribute.AttributeModifier;

/**
 * 物品种类
 */
public enum ItemType
{
    HEALTH_CRYSTAL(
            () -> new AttributeItem(
                    AttributeType.MAX_HEALTH,
                    new AttributeModifier("HealthCrystal", AttributeModifier.Operation.ADDITION, Constants.Common.HEALTH_CRYSTAL_VALUE)
            ),
            5,
            10
    ),

    ACCELERATE_FEATHER(
            () -> new AttributeItem(
                    AttributeType.SPEED,
                    new AttributeModifier("AccelerateFeather", AttributeModifier.Operation.MULTIPLICATION, Constants.Common.ACCELERATE_FEATHER_DEGREE)
            ),
            2,
            15
    ),
    HEALING_POTION(
            () -> new AttributeItem(
                    AttributeType.REGENERATION,
                    new AttributeModifier("HealingPotion", AttributeModifier.Operation.ADDITION, Constants.Common.HEALING_POTION_VALUE)
            ),
            5,
            20
    ),
    POWER_CRYSTAL(
            () -> new AttributeItem(
                    AttributeType.DAMAGE,
                    new AttributeModifier("PowerCrystal", AttributeModifier.Operation.ADDITION, Constants.Common.POWER_CRYSTAL_VALUE)
            ),
            5,
            20
    ),
    BOOMERANG_ATTACK(
            () -> new RechargeableComponentItem<>(
                    BoomerangComponent.class,
                    BoomerangComponent::new,
                    "Boomerang"
            ),
            5,
            10
    );

    /**
     * <p>物品种类的标识符，默认为枚举名称的小写字符串</p>
     * <p>该类物品的纹理路径为 <code>assets/textures/item/{id}.png</code></p>
     * <p>该类物品纹理图片尺寸必须为 32x32</p>
     * <p>该类物品的本地化键名为 <code>item.{id}</code></p>
     * <p>The identifier of the item, default to be lowercase of the enum name.</p>
     * <p>The texture of the item will be located at <code>assets/textures/item/{id}.png</code></p>
     * <p>The texture must be 32x32</p>
     * <p>The localized name of the item will be found by key <code>item.{id}</code></p>
     */
    public final String id;

    /**
     * <p>物品的权重</p>
     * <p>该值越大，商店刷新时越有可能出现该物品</p>
     * <p>The higher the weight, the more likely it is to be sold in the shop</p>
     */
    public final int weight;

    /**
     * <p>物品的价格</p>
     * <p>The price of the item</p>
     */
    public final int price;

    /**
     * <p>物品是否可堆叠</p>
     * <p>如果一个物品是可堆叠的，意味着玩家可以持有多份该物品，商店也会重复刷新该物品</p>
     * <p>If an item is stackable, it means that the player can hold multiple of the item, and the shop will also repeat the item</p>
     */
    public final boolean stackable;

    /**
     * <p>用来创建一个属于该物品种类的物品实例</p>
     * <p>Used to create a new item every time the shop refreshes</p>
     */
    private final ItemFactory itemFactory;

    // Lazy initialized fields
    private LazyValue<String> lazyName;
    private LazyValue<Texture> lazyTexture;

    ItemType(ItemFactory itemFactory, int price)
    {
        this(itemFactory, 0, price);
    }

    ItemType(ItemFactory itemFactory, int weight, int price)
    {
        this(itemFactory, weight, price, true);
    }

    ItemType(ItemFactory itemFactory, int weight, int price, boolean stackable)
    {
        this.price = price;
        this.id = StringUtil.lowercase(this.name());
        this.itemFactory = itemFactory;
        this.weight = weight;
        this.stackable = stackable;

        initLazyValues();
    }

    ItemType(String id, ItemFactory itemFactory, int weight, int price, boolean stackable)
    {
        this.id = id;
        this.itemFactory = itemFactory;
        this.weight = weight;
        this.price = price;
        this.stackable = stackable;

        initLazyValues();
    }

    private void initLazyValues()
    {
        this.lazyName = new LazyValue<>(() -> FXGL.localize("item.%s".formatted(this.id)));
        this.lazyTexture = new LazyValue<>(() -> FXGL.texture("item/%s.png".formatted(id)));
    }

    /**
     * @return 该类物品的本地化名称
     */
    public String getName()
    {
        return lazyName.get();
    }

    /**
     * @return 该类物品的纹理
     */
    public Texture getTexture()
    {
        return lazyTexture.get();
    }

    /**
     * 创建一个属于该物品种类的物品实例
     *
     * @return 属于该物品种类的物品实例
     */
    public Item createItem()
    {
        return itemFactory.create();
    }

    @FunctionalInterface
    public interface ItemFactory
    {
        Item create();
    }
}
