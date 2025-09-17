package top.offsetmonkey538.meshlib.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;
import top.offsetmonkey538.meshlib.platform.PlatformMain;
import top.offsetmonkey538.offsetconfig538.api.config.Config;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;

public class TestConfig implements Config {

    public AbstractThing<?> thingy = new Type1(new Type1.Data("defaultValue"));


    public interface AbstractThing<T> {
        String getType();
        T getData();
    }
    private static class Type1 implements AbstractThing<Type1.Data> {
        private final String value;

        public Type1(Data data) {
            this.value = data.value;
        }

        @Override
        public String getType() {
            return "type1";
        }

        @Override
        public Data getData() {
            return new Data(value);
        }

        @Override
        public String toString() {
            return "value: " + value;
        }

        public static final class Data {
            @SuppressWarnings("FieldMayBeFinal") // Think it needs to be non-final cause jankson
            public String value;

            @SuppressWarnings("unused") // Public no-args used by jankson i think
            public Data() {

            }

            private Data(String value) {
                this.value = value;
            }
        }
    }
    private static class Type2 implements AbstractThing<Type2.Data> {
        private final double differentValue;

        public Type2(Data data) {
            this.differentValue = data.differentValue;
        }

        @Override
        public String getType() {
            return "type2";
        }

        @Override
        public Data getData() {
            return new Data(differentValue);
        }

        @Override
        public String toString() {
            return "differentValue: " + differentValue;
        }

        public static final class Data {
            @SuppressWarnings("FieldMayBeFinal") // Think it needs to be non-final cause jankson
            public double differentValue;

            @SuppressWarnings("unused") // Public no-args used by jankson i think
            public Data() {

            }

            private Data(double value) {
                this.differentValue = value;
            }
        }
    }

    private static record ThingyDefinition<T>(Class<T> dataType, Function<T, AbstractThing<T>> thingyInitializer) {

    }

    @Override
    public void configureJankson(Jankson.@NotNull Builder builder) {
        // type to value holder type todo: actually have a registry type of thing for this
        final Map<String, ThingyDefinition<?>> testMap = Map.of(
                "type1", new ThingyDefinition<>(Type1.Data.class, Type1::new),
                "type2", new ThingyDefinition<>(Type2.Data.class, Type2::new)
        );

        builder.registerSerializer(AbstractThing.class, (abstractThing, marshaller) -> {
            final JsonObject result = (JsonObject) marshaller.serialize(abstractThing.getData());
            result.put("type", JsonPrimitive.of(abstractThing.getType()));
            return result;
        });
        builder.registerDeserializer(JsonObject.class, AbstractThing.class, (jsonObject, marshaller) -> {
            final String type = jsonObject.get(String.class, "type");
            @SuppressWarnings("unchecked") // It's proooobably a subclass of Object...
            ThingyDefinition<Object> thingyDefinition = (ThingyDefinition<Object>) testMap.get(type);
            final JsonObject dummyParent = new JsonObject();
            dummyParent.put("data", jsonObject);
            Object data = dummyParent.get(thingyDefinition.dataType, "data");

            return thingyDefinition.thingyInitializer.apply(data);
        });
    }

    @Override
    public @NotNull Path getFilePath() {
        return PlatformMain.getConfigDir().resolve(getId() + ".json");
    }

    @Override
    public @NotNull String getId() {
        return "test";
    }
}
