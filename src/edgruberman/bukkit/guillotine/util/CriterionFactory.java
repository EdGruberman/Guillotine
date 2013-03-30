package edgruberman.bukkit.guillotine.util;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

/**
 * since an interface can not define a constructor, implementations can
 * implement this class to allow for dynamic construction by class name
 */
public abstract class CriterionFactory<C extends Criterion<?>> {

    /** implementation factory method */
    public abstract C create(Plugin plugin, ConfigurationSection config) throws InstantiationException;



    /** decoupled standardized implementation construction from class name */
    public static <O extends Criterion<?>> O create(final String className, final Package defaultPackage, final Plugin plugin, final ConfigurationSection config)
            throws ClassNotFoundException, ClassCastException, IllegalArgumentException, SecurityException
                    , InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
            {

        // find the implementation class
        Class<?> implementation;
        try {
            implementation = Class.forName(defaultPackage.getName() + "." + className);
        } catch (final Exception e) {
            implementation = Class.forName(className);
        }
        @SuppressWarnings("unchecked")
        final Class<O> criterion = (Class<O>) implementation.asSubclass(Criterion.class);

        // find the nested factory and instantiate
        CriterionFactory<O> factory = null;
        for (final Class<?> nested : criterion.getDeclaredClasses()) {
            if (CriterionFactory.class.isAssignableFrom(nested)) {
                @SuppressWarnings("unchecked")
                final Class<? extends CriterionFactory<O>> cls = (Class<? extends CriterionFactory<O>>) nested.asSubclass(CriterionFactory.class);
                factory = cls.getConstructor().newInstance();
                break;
            }
        }
        if (factory == null) throw new ClassNotFoundException("unable to find CriterionFactory in: " + className);

        return factory.create(plugin, config);
    }

}
