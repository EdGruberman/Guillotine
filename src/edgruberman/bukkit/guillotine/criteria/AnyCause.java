package edgruberman.bukkit.guillotine.criteria;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import edgruberman.bukkit.guillotine.criteria.bases.CauseCriterion;
import edgruberman.bukkit.guillotine.util.CriterionFactory;

public class AnyCause implements CauseCriterion {

    private final String description;

    public AnyCause(final String description) {
        this.description = description;
    }

    @Override
    public boolean matches(final EntityDeathEvent death) {
        return true;
    }

    @Override
    public void destroy() {}

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{ description: " + this.description + " }";
    }



    public static class Factory extends CriterionFactory<AnyCause> {

        @Override
        public AnyCause create(final Plugin plugin, final ConfigurationSection config) {
            return new AnyCause(config.getName());
        }

    }

}
