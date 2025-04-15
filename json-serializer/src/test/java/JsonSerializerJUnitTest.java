import com.serializer.serialize.JsonSerializer;
import com.serializer.serialize.annotation.JsonField;
import com.serializer.serialize.annotation.JsonSerialization;
import com.serializer.serialize.exception.JsonSerializationException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonSerializerJUnitTest {

    @Test
    public void primitiveTypesTest() {
        Assert.assertEquals("integer test", "10", JsonSerializer.serialize(10));
        Assert.assertEquals("float test", "10.5", JsonSerializer.serialize(10.5f));
        Assert.assertEquals("double test", "10.5", JsonSerializer.serialize(10.5));
        Assert.assertEquals("boolean test", "true", JsonSerializer.serialize(true));
        Assert.assertEquals("string test", "\"something\"", JsonSerializer.serialize("something"));
    }

    @Test
    public void collectionTypesTest() {
        Assert.assertEquals("list of integers", "[1, 2, 3]", JsonSerializer.serialize(List.of(1, 2, 3)));
        Assert.assertEquals("set of string", "[2]", JsonSerializer.serialize(Set.of(2)));
        Assert.assertEquals("map of person", "[{\"key\": \"alice\", \"value\": {\"name\": \"Alice\", \"age\": 20}}]",
                JsonSerializer.serialize(Map.of("alice", new Person("Alice", 20))));
    }

    @Test
    public void supportedTypesTest() {
        Assert.assertEquals("person serialize", "{\"name\": \"Alice\", \"age\": 20}",
                JsonSerializer.serialize(new Person("Alice", 20)));
    }

    @Test
    public void unsupportedTypesTest() {
        try {
            JsonSerializer.serialize(new Thread());
        } catch (Throwable e) {
            Assert.assertEquals("Handling of unsupported types", JsonSerializationException.class, e.getClass());
        }
    }

    @Test
    public void recursiveTypesTest() {
        try {
            Recursive link = new Recursive();
            link.setLink(link);

            JsonSerializer.serialize(link);
        } catch (Throwable e) {
            Assert.assertEquals("Handling recursive references", JsonSerializationException.class, e.getClass());
        }
    }

    private static class Recursive {
        private Recursive link;

        public void setLink(Recursive link) {
            this.link = link;
        }
    }

    @JsonSerialization
    private record Person(@JsonField String name, @JsonField int age) {

    }
}
