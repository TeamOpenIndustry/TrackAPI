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
public final class PathingContext {
    private static final Map<String, TrackData<?>> registered = new ConcurrentHashMap<>();

    private final Map<TrackData<?>, Object> dataMap;

    //We have next found pos by default
    public final Vec3d pos;
    //And some built-in fields that are filled in
    //Moved distance between current pos and next pos
    public static final TrackData<Double> DELTA_MOVEMENT = createOrGetKey("delta_movement", Double.class, 0d);
    //And we expect you to fill in these
    //Track roll for stocks to do superelevation (rotated from middle of the rails)
    public static final TrackData<Double> ROLL_DEGREES = createOrGetKey("roll_degrees", Double.class, 0d);

    public PathingContext(Vec3d pos) {
        this.pos = Objects.requireNonNull(pos, "pos cannot be null");
        this.dataMap = new IdentityHashMap<>();
    }

    public <T> PathingContext with(TrackData<T> key, T value) {
        key.validate(value);
        dataMap.put(key, value);
        return this;
    }

    public <T> T get(TrackData<T> key) {
        Object value = dataMap.get(key);
        if (value == null) {
            return key.fallback();
        }
        return key.type().cast(value);
    }

    public void reset(TrackData<?> key) {
        dataMap.remove(key);
    }

    //For those don't want to use Minecraft's Vec3d directly
    public double x() {
        return pos.x;
    }
    public double y() {
        return pos.y;
    }
    public double z() {
        return pos.z;
    }

    public static <T> TrackData<T> createOrGetKey(String name, Class<T> type) {
        return createOrGetKey(name, type, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> TrackData<T> createOrGetKey(String name, Class<T> type, T fallback) {
        TrackData<?> existing = registered.get(name);
        if (existing != null) {
            if (!existing.type().equals(type)) {
                throw new IllegalStateException("Key '" + name + "' already registered with different type");
            }
            return (TrackData<T>) existing;
        }
        TrackData<T> data = new TrackData<>(name, type, fallback);
        registered.put(name, data);
        return data;
    }

    /**
     * Typed key for PathingContext's data storage
     * <p>
     * Please note this is only used in <code>IdentityHashMap</code>, and should not be used externally
     * @param <T> type of the value
     */
    public static class TrackData<T> {
        private final String name;
        private final Class<T> type;
        private final T fallback;

        private TrackData(String name, Class<T> type, T fallback) {
            this.name = Objects.requireNonNull(name);
            this.type = Objects.requireNonNull(type);
            this.fallback = fallback;
        }

        void validate(Object value) {
            if (value != null && !type.isInstance(value)) {
                throw new IllegalArgumentException("Invalid value for key '" + name + "', expected " + type.getSimpleName());
            }
        }

        public Class<T> type() {
            return type;
        }

        public T fallback() {
            return fallback;
        }

        @Override
        public String toString() {
            return "[Name:" + name + ",Type:" + type.getName() + "]";
        }
    }
}