package edgruberman.bukkit.guillotine.criteria.bases;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import edgruberman.bukkit.guillotine.util.EntitySubtype;

/**
 * reusable EntitySubtype comparison for {@link VictimCriterion}
 * or {@link CauseCriterion} which implements shared Criterion methods
 */
public class EntitySubtypeBase {

    private final String description;

    /** null is wildcard that matches any */
    private final EntitySubtype subtype;

    public EntitySubtypeBase(final ConfigurationSection config) throws InstantiationException {
        this.description = config.getName();
        final String est = config.getString("entity-subtype");
        if (est == null) {
            this.subtype = null;
            return;
        }

        try {
            this.subtype = EntitySubtype.of(est);
        } catch (final IllegalArgumentException e) {
            throw new InstantiationException("unrecognized EntityType: " + est + "; " + e);
        }
    }

    /** @return true if Entity is not null and matches EntitySubtype; false otherwise */
    public boolean matches(final Entity other) {
        if (other == null) return false; // can't match an entity that doesn't exist
        if (this.subtype == null) return true; // wildcard match for any entity subtype
        try {
            final EntitySubtype otherSubtype = EntitySubtype.of(other);
            return this.subtype.equals(otherSubtype);
        } catch (final IllegalArgumentException e) {
            return false;
        }
    }

    public void destroy() {}

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{ description: " + this.description + "; entity-subtype: " + this.subtype.getName() + " }";
    }

}
