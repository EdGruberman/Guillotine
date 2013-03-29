package edgruberman.bukkit.guillotine.criteria;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import edgruberman.bukkit.guillotine.SkullType;
import edgruberman.bukkit.guillotine.criteria.bases.VictimCriterion;
import edgruberman.bukkit.guillotine.util.CriterionFactory;

/** any entity that has a skull that can drop */
public class VictimHasSkull implements VictimCriterion {

    private final String description;

    public VictimHasSkull(final String description) {
        this.description = description;
    }

    @Override
    public boolean matches(final Entity victim) {
        try {
            SkullType.of(victim);
            return true;
        } catch (final IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void destroy() {}

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{ description: " + this.description + " }";
    }



    public static class Factory extends CriterionFactory<VictimHasSkull> {

        @Override
        public VictimHasSkull create(final Plugin plugin, final ConfigurationSection config) {
            return new VictimHasSkull(config.getName());
        }

    }

}
