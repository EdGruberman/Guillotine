package edgruberman.bukkit.guillotine.criteria;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import edgruberman.bukkit.guillotine.Executioner;
import edgruberman.bukkit.guillotine.criteria.bases.CauseCriterion;
import edgruberman.bukkit.guillotine.criteria.bases.EntitySubtypeBase;
import edgruberman.bukkit.guillotine.util.CriterionFactory;

public class KillerSubtype extends EntitySubtypeBase implements CauseCriterion {

    private KillerSubtype(final ConfigurationSection config) throws InstantiationException {
        super(config);
    }

    @Override
    public boolean matches(final EntityDeathEvent death) {
        return super.matches(Executioner.origin(death));
    }



    public static class Factory extends CriterionFactory<KillerSubtype> {

        @Override
        public KillerSubtype create(final Plugin plugin, final ConfigurationSection config) throws InstantiationException {
            return new KillerSubtype(config);
        }

    }

}
