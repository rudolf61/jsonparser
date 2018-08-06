package eu.json.parser;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TestJsonParser {

    @Test
    public void simpleTestje() throws IOException {
        String jsonString = "[{\"email\":\"jan@beton.nl\"},{\"password\":\"geheim\"}]";
        JsonReader reader = new JsonReader(jsonString);
        JsonToken token = null;
        String field = null;
        String value = null;
        while ((token = reader.nextToken()) != JsonToken.end_document) {

            if (token == JsonToken.field) {
                field = reader.getFieldName();
            }
            else if (token == JsonToken.value) {
                JsonValue tokenValue = reader.getValue();
                value = tokenValue.getStringValue();
                System.out.printf("Name/value pair is %s=%s%n", field, value);
            }
        }
    }

    @Test
    public void testJsonFile() throws IOException {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test1.json")) {
            JsonReader reader = new JsonReader(is, StandardCharsets.UTF_8);

            JsonToken token = null;
            String field = null;
            String value = null;

            while ((token = reader.nextToken()) != JsonToken.end_document) {

                if (token == JsonToken.field) {
                    System.out.printf("field name is %s%n",  reader.getFieldName());
                }
                else if (token == JsonToken.value) {
                    JsonValue tokenValue = reader.getValue();
                    value = tokenValue.getStringValue();
                    System.out.printf("Value is %s%n", value);
                }
            }
        }
    }

}
