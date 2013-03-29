package edgruberman.bukkit.guillotine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

/** drops heads from bodies **/
public final class Executioner implements Listener {

    private static final Random RNG = new Random();

    private final Plugin plugin;

    private final List<DecapitationRule> rules = new ArrayList<DecapitationRule>();

    Executioner(final Plugin plugin) {
        this.plugin = plugin;
    }

    public void addRule(final DecapitationRule rule) {
        this.rules.add(rule);
    }

    public List<DecapitationRule> getRules() {
        return this.rules;
    }

    private DecapitationRule applicable(final EntityDeathEvent death) {
        for (final DecapitationRule rule : this.rules) {
            if (rule.applies(death)) return rule;
        }
        return DecapitationRule.NOT_APPLICABLE;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR) // give everything else a chance to cancel it
    public void onEntityDeathByEntity(final EntityDeathEvent death) {
        final DecapitationRule applicable = this.applicable(death);
        if (applicable == DecapitationRule.NOT_APPLICABLE) return;

        final Entity victim = death.getEntity();
        final Entity killer = Executioner.origin(death);
        final double chance = applicable.getChance(killer);

        // randomize creation of head
        final double picked = chance < 1 ? Executioner.RNG.nextDouble() : 1;
        if (chance < picked) {
            if (this.plugin.getLogger().isLoggable(Level.FINEST)) {
                this.plugin.getLogger().log(Level.FINEST, "Missed head drop chance; rule: {0}, victim: {1}, killer: {2}, cause: {3}, chance: {4,number,#.#%}, picked: {5,number,#.##%}"
                        , new Object[] { applicable.getDescription(), Executioner.describe(victim), Executioner.describe(killer), Executioner.describe(victim.getLastDamageCause()), chance, picked });
            }
            return;
        }

        // drop head
        final Location drop = victim.getLocation();
        drop.getWorld().dropItemNaturally(drop, SkullType.asItemStack(victim));
        if (this.plugin.getLogger().isLoggable(Level.FINER)) {
            this.plugin.getLogger().log(Level.FINER, "Head dropped; rule: {0}, victim: {1}, killer: {2}, cause: {3}, chance: {4,number,#.#%}, picked: {5,number,#.##%}"
                    , new Object[] { applicable.getDescription(), Executioner.describe(victim), Executioner.describe(killer), Executioner.describe(victim.getLastDamageCause()), chance, picked });
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR) // give everything else a chance to cancel it
    public void onPlayerSkullBlockRightClick(final PlayerInteractEvent interact) {
        if (interact.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (interact.getClickedBlock().getTypeId() != Material.SKULL.getId()) return;
        final Skull state = (Skull) interact.getClickedBlock().getState();
        if (state.getSkullType() != org.bukkit.SkullType.PLAYER) return;

        Main.courier.send(interact.getPlayer(), "describe", state.getOwner());
    }



    /** @return killer {@link #origin(Entity) origin} */
    public static Entity origin(final EntityDeathEvent death) {
        final EntityDamageEvent last = death.getEntity().getLastDamageCause();
        if (!(last instanceof EntityDamageByEntityEvent)) return null;

        final EntityDamageByEntityEvent lastByEntity = (EntityDamageByEntityEvent) last;
        final Entity damager = lastByEntity.getDamager();

        return Executioner.origin(damager);
    }

    /** @return origin of entity (shooter for projectiles, null for dispensers) */
    private static Entity origin(final Entity entity) {
        if (entity instanceof Projectile) {
            final Projectile projectile = (Projectile) entity;
            return projectile.getShooter();
        }

        return entity;
    }

    /** @return human readable description of {@link #origin(Entity) origin} */
    private static String describe(final Entity entity) {
        if (entity == null) {
            return null;

        } else if (entity instanceof HumanEntity) {
            final HumanEntity human = (HumanEntity) entity;
            final int level = human.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
            return entity.getType().name() + "(" + human.getName() + ( level > 0 ? "/" + Enchantment.LOOT_BONUS_MOBS.getName() + ":" + level : "" ) + ")";

        }

        return entity.getType().name();
    }

    private static String describe(final EntityDamageEvent damage) {
        String block = null;
        if (damage instanceof EntityDamageByBlockEvent) {
            final EntityDamageByBlockEvent dbb = (EntityDamageByBlockEvent) damage;
            block = dbb.getDamager().getType().name() + ( dbb.getDamager().getData() > 0 ? "/" + dbb.getDamager().getData() : "" );
        }

        return damage.getCause().name() + ( block != null ? " (" + block + ")" : "" );
    }

}
