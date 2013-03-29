package edgruberman.bukkit.guillotine;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

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



    /** @throws IllegalArgumentException when stack does not contain a supported SkullType */
    public static SkullType of(final ItemStack stack) throws IllegalArgumentException {
        if (stack.getType().getId() != Material.SKULL_ITEM.getId()) throw new IllegalArgumentException("ItemStack is not SKULL_ITEM");

        final short data = stack.getDurability();
        for (final SkullType type : SkullType.values()) {
            if (type.itemData == data) {
                return type;
            }
        }

        throw new IllegalArgumentException("unsupported ItemStack data: " + data);
    }

    /** @throws IllegalArgumentException when entity does not have a supported SkullType */
    public static SkullType of(final Entity entity) throws IllegalArgumentException {
        switch (entity.getType()) {
        case SKELETON:
            switch (((Skeleton) entity).getSkeletonType()) {
            case WITHER: return SkullType.WITHER;
            default: return SkullType.SKELETON;
            }
        case ZOMBIE: return SkullType.ZOMBIE;
        case PLAYER: return SkullType.HUMAN;
        case CREEPER: return SkullType.CREEPER;
        default: throw new IllegalArgumentException("SkullType is not supported for EntityType: " + entity.getType().name());
        }
    }

    /**
     * singular {@link Material#SKULL_ITEM SKULL_ITEM} with owner set for Player entities
     *
     * @throws IllegalArgumentException when entity does not have a supported SkullType
     */
    public static ItemStack asItemStack(final Entity entity) throws IllegalArgumentException {
        return SkullType.asItemStack(entity, 1);
    }

    /**
     * ItemStack of {@link Material#SKULL_ITEM SKULL_ITEM}s matching entity with owner set for Player entities
     *
     * @throws IllegalArgumentException when entity does not have a supported SkullType
     */
    public static ItemStack asItemStack(final Entity entity, final int quantity) throws IllegalArgumentException {
        final SkullType type = SkullType.of(entity);
        final ItemStack skull = type.toItemStack();
        if (type.equals(SkullType.HUMAN)) {
            final String owner = ( entity instanceof HumanEntity ? ((HumanEntity) entity).getName() : null );
            SkullType.setOwner(skull, owner);
        }
        return skull;
    }

    public static void setOwner(final ItemStack stack, final String owner) {
        final SkullType type = SkullType.of(stack);
        if (!type.equals(SkullType.HUMAN)) throw new IllegalArgumentException("ItemStack is not a HUMAN skull");

        final SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwner(owner);
        stack.setItemMeta(meta);
    }

}
