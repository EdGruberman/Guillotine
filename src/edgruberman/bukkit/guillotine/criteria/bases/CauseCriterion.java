package edgruberman.bukkit.guillotine.criteria.bases;

import org.bukkit.event.entity.EntityDeathEvent;

import edgruberman.bukkit.guillotine.util.Criterion;

public interface CauseCriterion extends Criterion<EntityDeathEvent> {

    @Override
    public boolean matches(EntityDeathEvent death);

}
