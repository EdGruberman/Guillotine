package edgruberman.bukkit.guillotine;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;

public enum SkullType {

      SKELETON(0)
    , WITHER(1)
    , ZOMBIE(2)
    , HUMAN(3)
    , CREEPER(4)
    ;

    private final int itemData;

    private SkullType(final int itemData) {
        this.itemData = itemData;
    }

    public boolean matches(final ItemStack stack) {
        return stack.getDurability() == this.itemData;
    }

    public ItemStack toItemStack() {
        return this.toItemStack(1);
    }

    public ItemStack toItemStack(final int quantity) {
        return new ItemStack(Material.SKULL_ITEM, quantity, (short) this.itemData);
    }

    public static SkullType of(final Entity entity) {
        switch (entity.getType()) {
        case SKELETON:
            switch (((Skeleton) entity).getSkeletonType()) {
            case WITHER: return SkullType.WITHER;
            default: return SkullType.SKELETON;
            }
        case ZOMBIE: return SkullType.ZOMBIE;
        case PLAYER: return SkullType.HUMAN;
        case CREEPER: return SkullType.CREEPER;
        default: return null;
        }
    }

}
