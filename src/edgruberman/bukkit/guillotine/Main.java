package edgruberman.bukkit.guillotine;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;

import edgruberman.bukkit.guillotine.commands.Reload;
import edgruberman.bukkit.guillotine.criteria.AnyCause;
import edgruberman.bukkit.guillotine.criteria.KillerType;
import edgruberman.bukkit.guillotine.criteria.VictimHasSkull;
import edgruberman.bukkit.guillotine.criteria.VictimType;
import edgruberman.bukkit.guillotine.criteria.bases.CauseCriterion;
import edgruberman.bukkit.guillotine.criteria.bases.VictimCriterion;
import edgruberman.bukkit.guillotine.messaging.ConfigurationCourier;
import edgruberman.bukkit.guillotine.util.Criterion;
import edgruberman.bukkit.guillotine.util.CriterionFactory;
import edgruberman.bukkit.guillotine.util.CustomPlugin;

public final class Main extends CustomPlugin {

    /** implicit victims entry class for rule */
    private static final Class<? extends VictimCriterion> IMPLICIT_VICTIM_CLASS = VictimType.class;

    /** implicit causes entry class for rule */
    private static final Class<? extends CauseCriterion> IMPLICIT_CAUSE_CLASS = KillerType.class;

    /** default victim for rule when no victims defined */
    private static final VictimCriterion DEFAULT_VICTIM = new VictimHasSkull(null);

    /** default cause for rule when no causes defined */
    private static final CauseCriterion DEFAULT_CAUSE = new AnyCause(null);

    /** where to search for Criterion without fully qualified names */
    private static final Package DEFAULT_CRITERIA = Package.getPackage(Main.class.getPackage().getName() + ".criteria");

    public static ConfigurationCourier courier;

    @Override
    public void onLoad() { this.putConfigMinimum("2.1.0"); }

    @Override
    public void onEnable() {
        this.reloadConfig();
        Main.courier = ConfigurationCourier.Factory.create(this).build();

        final Executioner executioner = new Executioner(this);
        final ConfigurationSection decaps = this.getConfig().getConfigurationSection("decapitations");
        for (final String key : decaps.getKeys(false)) {
            final ConfigurationSection section = decaps.getConfigurationSection(key);
            final boolean drop = section.getBoolean("drop", DecapitationRule.DEFAULT_DROP);
            final double chance = drop ? section.getDouble("chance", 100D) / 100D : DecapitationRule.DISABLE_CHANCE;
            final double lootingFactor = drop ? section.getDouble("looting-factor", DecapitationRule.DEFAULT_LOOTING_FACTOR) : DecapitationRule.DISABLE_LOOTING_FACTOR;
            final DecapitationRule rule = new DecapitationRule(key, chance, lootingFactor);

            final ConfigurationSection victims = section.getConfigurationSection("victims");
            rule.getVictims().addAll(this.parseCriteria(victims, Main.IMPLICIT_VICTIM_CLASS, Main.DEFAULT_VICTIM));
            final ConfigurationSection causes = section.getConfigurationSection("causes");
            rule.getCauses().addAll(this.parseCriteria(causes, Main.IMPLICIT_CAUSE_CLASS, Main.DEFAULT_CAUSE));

            executioner.addRule(rule);
        }
        for (final DecapitationRule rule : executioner.getRules()) this.getLogger().log(Level.CONFIG, "{0}", rule);

        Bukkit.getPluginManager().registerEvents(executioner, this);

        this.getCommand("guillotine:reload").setExecutor(new Reload(this));
    }

    @Override
    public void onDisable() {
        Main.courier = null;
        HandlerList.unregisterAll(this);
    }

    private <C> List<Criterion<C>> parseCriteria(final ConfigurationSection section, final Class<? extends Criterion<C>> defaultClass, final Criterion<C> defaultCriterion) {
        final List<Criterion<C>> result = new ArrayList<Criterion<C>>();
        if (section == null) {
            result.add(defaultCriterion);
            return result;
        }

        for (final String key : section.getKeys(false)) {
            final ConfigurationSection entry = section.getConfigurationSection(key);
            final String className = entry.getString("class", defaultClass.getSimpleName());

            try {
                final Criterion<C> criterion = CriterionFactory.create(className, Main.DEFAULT_CRITERIA, this, entry);
                result.add(criterion);
            } catch (final Exception e) {
                this.getLogger().log(Level.WARNING, "Unable to create Criterion: {0}; {1}", new Object[] { entry.getCurrentPath(), e });
                this.getLogger().log(Level.FINE, "Criterion creation exception detail", e);
                continue;
            }
        }

        if (result.size() == 0) result.add(defaultCriterion);

        return result;
    }

}
