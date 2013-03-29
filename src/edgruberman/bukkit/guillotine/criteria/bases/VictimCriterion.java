package edgruberman.bukkit.guillotine.criteria.bases;

import org.bukkit.entity.Entity;

import edgruberman.bukkit.guillotine.util.Criterion;

public interface VictimCriterion extends Criterion<Entity> {

    @Override
    public boolean matches(Entity victim);

}
