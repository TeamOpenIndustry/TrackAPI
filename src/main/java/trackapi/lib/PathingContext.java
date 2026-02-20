package trackapi.lib;

import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Helper class for bundled pathing data transformation
 */
public final class PathingContext {
    private static final Map<String, TrackData<?>> registered = new HashMap<>();

    private final Map<TrackData<?>, Object> dataMap;

    //We have pos by default
    private final Vec3d pos;
    //And some built-in fields that are filled in
    public static final TrackData<Double> MOVEMENT = createKey("movement", Double.class, 0d);
    //And we expect you to fill in this
    public static final TrackData<Double> ROLL_DEGREES = createKey("roll_degrees", Double.class, 0d);

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

    public Vec3d pos() {
        return pos;
    }

    //And for those don't want to use Minecraft's Vec3d directly
    public double x() {
        return pos.x;
    }
    public double y() {
        return pos.y;
    }
    public double z() {
        return pos.z;
    }

    public static <T> TrackData<T> createKey(String name, Class<T> type) {
        return createKey(name, type, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> TrackData<T> createKey(String name, Class<T> type, T fallback) {
        return (TrackData<T>) registered.computeIfAbsent(name, str -> new TrackData<>(name, type, fallback));
    }

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
        public int hashCode() {
            return Objects.hash(name, type, fallback);
        }

        @Override
        public String toString() {
            return "[Name:" + name + ",Type:" + type.getName() + "]";
        }
    }
}