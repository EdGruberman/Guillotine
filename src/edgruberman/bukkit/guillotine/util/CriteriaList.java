package edgruberman.bukkit.guillotine.util;

import java.util.ArrayList;

public class CriteriaList<T> extends ArrayList<Criterion<T>> {
    private static final long serialVersionUID = 1L;

    /** @return true only if at least one Criterion {@link Criterion#matches matches} */
    public boolean matchesAny(final T t) {
        for (final Criterion<T> c : this){
            if (c.matches(t)) {
                return true;
            }
        }
        return false;
    }

    /** {@link Criterion#destroy destroy} each Criterion and then {@link #clear} */
    public void destroy() {
        for (final Criterion<T> c :this) {
            c.destroy();
        }
        this.clear();
    }

}
