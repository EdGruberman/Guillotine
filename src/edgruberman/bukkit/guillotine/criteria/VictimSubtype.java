package edgruberman.bukkit.guillotine.criteria;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import edgruberman.bukkit.guillotine.criteria.bases.EntitySubtypeBase;
import edgruberman.bukkit.guillotine.criteria.bases.VictimCriterion;
import edgruberman.bukkit.guillotine.util.CriterionFactory;

public class VictimSubtype extends EntitySubtypeBase implements VictimCriterion {

    private VictimSubtype(final ConfigurationSection config) throws InstantiationException {
        super(config);
    }

    @Override
    public boolean matches(final Entity victim) {
        return super.matches(victim);
    }



    public static class Factory extends CriterionFactory<VictimSubtype> {

        @Override
        public VictimSubtype create(final Plugin plugin, final ConfigurationSection config) throws InstantiationException {
            return new VictimSubtype(config);
        }

    }

}
