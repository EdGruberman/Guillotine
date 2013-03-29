package edgruberman.bukkit.guillotine.criteria;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import edgruberman.bukkit.guillotine.Executioner;
import edgruberman.bukkit.guillotine.criteria.bases.CauseCriterion;
import edgruberman.bukkit.guillotine.criteria.bases.EntityTypeBase;
import edgruberman.bukkit.guillotine.util.CriterionFactory;

public class KillerType extends EntityTypeBase implements CauseCriterion {

    private KillerType(final ConfigurationSection config) throws InstantiationException {
        super(config);
    }

    @Override
    public boolean matches(final EntityDeathEvent death) {
        return super.matches(Executioner.origin(death));
    }



    public static class Factory extends CriterionFactory<KillerType> {

        @Override
        public KillerType create(final Plugin plugin, final ConfigurationSection config) throws InstantiationException {
            return new KillerType(config);
        }

    }

}
