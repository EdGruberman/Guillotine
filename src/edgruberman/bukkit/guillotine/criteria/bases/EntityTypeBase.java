package edgruberman.bukkit.guillotine.criteria.bases;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * reusable EntityType comparison for {@link VictimCriterion}
 * or {@link CauseCriterion} which implements shared Criterion methods
 */
public class EntityTypeBase {

    private final String description;

    /** null is wildcard that matches any */
    private final EntityType type;

    public EntityTypeBase(final ConfigurationSection config) throws InstantiationException {
        this.description = config.getName();
        final String et = config.getString("entity-type");
        try {
            this.type = EntityType.valueOf(et);
        } catch (final IllegalArgumentException e) {
            throw new InstantiationException("Unrecognized EntityType: " + et + "; " + e);
        }
    }

    /** @return true if EntityType is not null and matches; false otherwise */
    public boolean matches(final Entity other) {
        if (other == null) return false; // can't match an entity that doesn't exist
        if (this.type == null) return true; // wildcard match for any entity type
        return this.type.equals(other.getType());
    }

    public void destroy() {}

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{ description: " + this.description + "; entity-type: " + this.type.name() + " }";
    }

}
