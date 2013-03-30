package edgruberman.bukkit.guillotine.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Slime;

public abstract class EntitySubtype {

    private static Map<String, EntitySubtype> known = new HashMap<String, EntitySubtype>();

    public static final EntitySubtype SLIME_BIG = new BigSlime("SLIME_BIG");
    public static final EntitySubtype SLIME_SMALL = new SmallSlime("SLIME_SMALL");
    public static final EntitySubtype SLIME_TINY = new TinySlime("SLIME_TINY");
    public static final EntitySubtype MAGMA_CUBE_BIG = new BigMagmaCube("MAGMA_CUBE_BIG");
    public static final EntitySubtype MAGMA_CUBE_SMALL = new SmallMagmaCube("MAGMA_CUBE_SMALL");
    public static final EntitySubtype MAGMA_CUBE_TINY = new TinyMagmaCube("MAGMA_CUBE_TINY");
    public static final EntitySubtype CREEPER_POWERED = new PoweredCreeper("CREEPER_POWERED");
    public static final EntitySubtype SKELETON_WITHER = new WitherSkeleton("SKELETON_WITHER");
    public static final EntitySubtype SKELETON_NORMAL = new NormalSkeleton("SKELETON_NORMAL");



    final String name;

    protected EntitySubtype(final String name) {
        this.name = name;
        EntitySubtype.known.put(name, this);
    }

    public String getName() {
        return this.name;
    }

    public abstract boolean matches(final Entity entity);



    public static EntitySubtype of(final Entity entity) throws IllegalArgumentException {
        for (final EntitySubtype subtype : EntitySubtype.known.values()) {
            if (subtype.matches(entity)) {
                return subtype;
            }
        }

        throw new IllegalArgumentException("entity not supported");
    }

    public static EntitySubtype of(final String name) throws IllegalArgumentException {
        final EntitySubtype result = EntitySubtype.known.get(name);
        if (result == null) throw new IllegalArgumentException("unknown name: " + name);
        return result;
    }



    public abstract static class Subtype<E extends Entity> extends EntitySubtype {

        private final Class<E> type;

        protected Subtype(final String name, final Class<E> type) {
            super(name);
            this.type = type;
        }

        @SuppressWarnings("unchecked")
        @Override
        public final boolean matches(final Entity entity) {
            return this.type.isAssignableFrom(entity.getClass()) && this.matchesSubtype((E) entity);
        }

        protected abstract boolean matchesSubtype(E e);

    }



    public static class BigMagmaCube extends Subtype<MagmaCube> {

        public BigMagmaCube(final String name) {
            super(name, MagmaCube.class);
        }

        @Override
        public boolean matchesSubtype(final MagmaCube cube) {
            return cube.getSize() == 4;
        }

    }



    public static class SmallMagmaCube extends Subtype<MagmaCube> {

        public SmallMagmaCube(final String name) {
            super(name, MagmaCube.class);
        }

        @Override
        public boolean matchesSubtype(final MagmaCube cube) {
            return cube.getSize() == 2;
        }

    }



    public static class TinyMagmaCube extends Subtype<MagmaCube> {

        public TinyMagmaCube(final String name) {
            super(name, MagmaCube.class);
        }

        @Override
        public boolean matchesSubtype(final MagmaCube cube) {
            return cube.getSize() == 1;
        }

    }


    public static class BigSlime extends Subtype<Slime> {

        public BigSlime(final String name) {
            super(name, Slime.class);
        }

        @Override
        public boolean matchesSubtype(final Slime slime) {
            return slime.getSize() == 4;
        }

    }



    public static class SmallSlime extends Subtype<Slime> {

        public SmallSlime(final String name) {
            super(name, Slime.class);
        }

        @Override
        public boolean matchesSubtype(final Slime slime) {
            return slime.getSize() == 2;
        }

    }



    public static class TinySlime extends Subtype<Slime> {

        public TinySlime(final String name) {
            super(name, Slime.class);
        }

        @Override
        public boolean matchesSubtype(final Slime slime) {
            return slime.getSize() == 1;
        }

    }



    public static class NormalSkeleton extends Subtype<Skeleton> {

        public NormalSkeleton(final String name) {
            super(name, Skeleton.class);
        }

        @Override
        public boolean matchesSubtype(final Skeleton skeleton) {
            return skeleton.getSkeletonType() == SkeletonType.NORMAL;
        }

    }



    public static class WitherSkeleton extends Subtype<Skeleton> {

        public WitherSkeleton(final String name) {
            super(name, Skeleton.class);
        }

        @Override
        public boolean matchesSubtype(final Skeleton skeleton) {
            return skeleton.getSkeletonType() == SkeletonType.WITHER;
        }

    }



    public static class PoweredCreeper extends Subtype<Creeper> {

        public PoweredCreeper(final String name) {
            super(name, Creeper.class);
        }

        @Override
        public boolean matchesSubtype(final Creeper creeper) {
            return creeper.isPowered();
        }

    }

}
