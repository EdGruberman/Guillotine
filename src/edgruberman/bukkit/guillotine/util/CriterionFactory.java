package edgruberman.bukkit.guillotine.util;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

/**
 * since an interface can not define a constructor, implementations can
 * implement this class to allow for dynamic construction by class name
 */
public abstract class CriterionFactory<C extends Criterion<?>> {

    /** decoupled standardized implementation construction from class name */
    public static Criterion<?> create(final String className, final Package defaultPackage, final Plugin plugin, final ConfigurationSection config)
            throws ClassNotFoundException, ClassCastException, IllegalArgumentException, SecurityException
                    , InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
            {

        // find the implementation class
        Class<?> implementation;
        try {
            implementation = Class.forName(defaultPackage.getName() + "." + className).asSubclass(Criterion.class);
        } catch (final Exception e) {
            implementation = Class.forName(className).asSubclass(Criterion.class);
        }

        // find the nested factory and instantiate
        CriterionFactory<?> factory = null;
        for (final Class<?> nested : implementation.getDeclaredClasses()) {
            if (CriterionFactory.class.isAssignableFrom(nested)) {
                @SuppressWarnings("unchecked")
                final Class<? extends CriterionFactory<?>> cls = (Class<? extends CriterionFactory<?>>) nested.asSubclass(CriterionFactory.class);
                factory = cls.getConstructor().newInstance();
                break;
            }
        }
        if (factory == null) throw new ClassNotFoundException("unable to find CriterionFactory in: " + className);

        // instantiate the criterion
        return factory.create(plugin, config);
    }



    /** implementation factory method */
    public abstract C create(Plugin plugin, ConfigurationSection config) throws InstantiationException;

}
