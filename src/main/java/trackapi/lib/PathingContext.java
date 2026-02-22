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
    private final Map<TrackData<?>, Object> dataMap;

    //We have next found pos by default
    public final Vec3 pos;
    //And some built-in fields that are filled in
    //Moved distance between current pos and next pos
    public static final TrackData<Vec3> PREV_POS = createOrGetKey("prevPos", Vec3.class);
    public static final TrackData<Double> DELTA_MOVEMENT = createOrGetKey("deltaMovement", Double.class);
    public static final TrackData<Double> GAUGE = createOrGetKey("gauge", Double.class);
    //And we expect you to fill in these
    //Track roll for stocks to do superelevation (rotated from middle of the rails)
    public static final TrackData<Double> ROLL_DEGREES = createOrGetKey("rollDegrees", Double.class);

    //Wrapper for vanilla Vec3d
    public PathingContext(Vec3d pos) {
        this(new Vec3(pos));
    }

    public PathingContext(Vec3 pos) {
        this.pos = Objects.requireNonNull(pos, "pos cannot be null");
        this.dataMap = new IdentityHashMap<>();
    }

    public <T> PathingContext with(TrackData<T> key, T value) {
        key.validate(value);
        dataMap.put(key, value);
        return this;
    }

    public <T> T get(TrackData<T> key) {
        return key.type().cast(dataMap.get(key));
    }

    public void reset(TrackData<?> key) {
        dataMap.remove(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> TrackData<T> createOrGetKey(String name, Class<T> type) {
        TrackData<?> existing = TrackData.registered.get(name);
        if (existing != null) {
            if (!existing.type().equals(type)) {
                throw new IllegalStateException("Key '" + name + "' already registered with different type");
            }
            return (TrackData<T>) existing;
        }
        TrackData<T> data = new TrackData<>(name, type);
        TrackData.registered.put(name, data);
        return data;
    }

    /**
     * Typed key for PathingContext's data storage
     * <p>
     * Please note this is only used in <code>IdentityHashMap</code>, and should not be used externally
     * @param <T> type of the value
     */
    public static class TrackData<T> {
        private static final Map<String, TrackData<?>> registered = new ConcurrentHashMap<>();
        private final String name;
        private final Class<T> type;

        private TrackData(String name, Class<T> type) {
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