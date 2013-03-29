package edgruberman.bukkit.guillotine.util;

public interface Criterion<T> {

    public abstract boolean matches(T t);

    public abstract void destroy();

}
