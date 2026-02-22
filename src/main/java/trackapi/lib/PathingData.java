package trackapi.lib;

import net.minecraft.util.math.Vec3d;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Helper class for transferring bundled pathing data
 * <p>
 * Users could define associated data to pass from track to stock
 */
public class PathingData {

    //We have next found pos and roll by default
    public final Vec3 position;
    //Clockwise is positive when facing the direction of motion.
    public final double roll;
    private final Map<Key<?>, Object> extension;
    //And some common fields
    //Moved distance between current pos and next pos
    public static final Key<Double> DELTA_MOVEMENT = createOrGetKey("deltaMovement", Double.class);
    public static final Key<Double> DELTA_ROLL = createOrGetKey("deltaRoll", Double.class);

    //Wrapper for vanilla Vec3d
    public PathingData(Vec3d position, double roll) {
        this(new Vec3(position), roll);
    }

    public PathingData(Vec3 position, double roll) {
        this.position = Objects.requireNonNull(position, "position cannot be null");
        this.roll = roll;
        this.extension = new IdentityHashMap<>();
    }

    public <T> PathingData with(Key<T> key, T value) {
        key.validate(value);
        extension.put(key, value);
        return this;
    }

    public <T> T get(Key<T> key) {
        return key.type().cast(extension.get(key));
    }

    public void remove(Key<?> key) {
        extension.remove(key);
    }

    public boolean containsKey(Key<?> key) {
        return extension.containsKey(key);
    }

    public PathingData fromPrev(PathingData inputData) {
        return this.with(DELTA_MOVEMENT, position.toVanilla().distanceTo(inputData.position.toVanilla()))
                   .with(DELTA_ROLL, roll - inputData.roll);
    }

    @SuppressWarnings("unchecked")
    public static <T> Key<T> createOrGetKey(String name, Class<T> type) {
        Key<?> existing = Key.registered.get(name);
        if (existing != null) {
            if (!existing.type().equals(type)) {
                throw new IllegalStateException("Key '" + name + "' already registered with different type");
            }
            return (Key<T>) existing;
        }
        Key<T> key = new Key<>(name, type);
        Key.registered.put(name, key);
        return key;
    }

    /**
     * Typed key for PathingData's data storage
     * <p>
     * Please note this is only used in <code>IdentityHashMap</code>, and should not be used externally
     * @param <T> type of the value
     */
    public static class Key<T> {
        private static final Map<String, Key<?>> registered = new ConcurrentHashMap<>();
        private final String name;
        private final Class<T> type;

        private Key(String name, Class<T> type) {
            this.name = Objects.requireNonNull(name);
            this.type = Objects.requireNonNull(type);
        }

        void validate(Object value) {
            if (value != null && !type.isInstance(value)) {
                throw new IllegalArgumentException("Invalid value for key '" + name + "', expected " + type.getSimpleName());
            }
        }

        public Class<T> type() {
            return type;
        }

        @Override
        public String toString() {
            return "[Name:" + name + ",Type:" + type.getName() + "]";
        }
    }
}