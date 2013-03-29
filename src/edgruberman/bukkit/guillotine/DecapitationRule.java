package edgruberman.bukkit.guillotine;

import java.text.MessageFormat;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import edgruberman.bukkit.guillotine.criteria.bases.CauseCriterion;
import edgruberman.bukkit.guillotine.criteria.bases.VictimCriterion;
import edgruberman.bukkit.guillotine.util.CriteriaList;

public class DecapitationRule {

    public static final double DEFAULT_LOOTING_FACTOR = 0.005D;
    public static final DecapitationRule NOT_APPLICABLE = new NotApplicable();

    private final String description;
    private final double chance;
    private final double lootingFactor;
    private final CriteriaList<Entity> victims = new CriteriaList<Entity>();
    private final CriteriaList<EntityDeathEvent> causes = new CriteriaList<EntityDeathEvent>();

    DecapitationRule(final String description, final double chance, final double lootingFactor) {
        this.description = description;
        this.chance = chance;
        this.lootingFactor = lootingFactor;
    }

    public String getDescription() {
        return this.description;
    }

    public double getChance() {
        return this.chance;
    }

    public double getLootingFactor() {
        return this.lootingFactor;
    }

    public CriteriaList<Entity> getVictims() {
        return this.victims;
    }

    public CriteriaList<EntityDeathEvent> getCauses() {
        return this.causes;
    }

    public double getChance(final Entity killer) {
        return this.chance + this.bonus(killer);
    }

    public void addVictim(final VictimCriterion c) {
        this.victims.add(c);
    }

    public  void addCause(final CauseCriterion e){
        this.causes.add(e);
    }

    public boolean applies(final EntityDeathEvent death) {
        return (this.victims.matchesAny(death.getEntity()) && this.causes.matchesAny(death));
    }

    private double bonus(final Entity entity) {
        if (this.lootingFactor <= 0) return 0;
        if (entity == null) return 0;
        if (!(entity instanceof HumanEntity)) return 0;
        final ItemStack held = ((HumanEntity) entity).getItemInHand();
        if (held == null || held.getType().getId() == Material.AIR.getId()) return 0;
        return held.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) * this.lootingFactor;
    }

    @Override
    public String toString() {
        return MessageFormat.format("DecapitationRule'{' description: {0}; chance: {1,number,#.#%}; looting-factor: {2,number,#.#%}; victims: {3}; causes: {4} '}'"
                , new Object[] { this.description, this.chance, this.lootingFactor, this.victims, this.causes });
    }



    public static class NotApplicable extends DecapitationRule {

        private static final double NO_CHANCE = 0D;

        NotApplicable() {
            super(null, NotApplicable.NO_CHANCE, DecapitationRule.DEFAULT_LOOTING_FACTOR);
        }

    }

}
