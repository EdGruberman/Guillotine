package edgruberman.bukkit.guillotine.criteria;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import edgruberman.bukkit.guillotine.criteria.bases.EntityTypeBase;
import edgruberman.bukkit.guillotine.criteria.bases.VictimCriterion;
import edgruberman.bukkit.guillotine.util.CriterionFactory;

public class VictimType extends EntityTypeBase implements VictimCriterion {

    private VictimType(final ConfigurationSection config) throws InstantiationException {
        super(config);
    }

    @Override
    public boolean matches(final Entity victim) {
        return super.matches(victim);
    }



    public static class Factory extends CriterionFactory<VictimType> {

        @Override
        public VictimType create(final Plugin plugin, final ConfigurationSection config) throws InstantiationException {
            return new VictimType(config);
        }

    }

}
