package eu.json.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestJsonParser {

    @Test
    public void simpleTestje() throws IOException {
        String jsonString = "[{\"email\":\"jan@beton.nl\"},{\"password\":\"geheim\"}]";
        JsonReader reader = new JsonReader(jsonString);
        JsonToken token = null;
        String field = null;
        String value = null;

        Map<String, String> collect = new HashMap<>();

        while ((token = reader.nextToken()) != JsonToken.end_document) {

            if (token == JsonToken.field) {
                field = reader.getFieldName();
            }
            else if (token == JsonToken.value) {
                JsonValue tokenValue = reader.getValue();
                value = tokenValue.getStringValue();
                collect.put(field, value);
            }
        }

        Assert.assertEquals("jan@beton.nl", collect.get("email"));
        Assert.assertEquals("geheim", collect.get("password"));
    }

    @Test
    public void testJsonFile1() throws IOException {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test1.json")) {
            JsonReader reader = new JsonReader(is, StandardCharsets.UTF_8);

            JsonToken token = null;
            String field = null;
            String value = null;

            Map<String, List<Object>> result = new HashMap<>();

            while ((token = reader.nextToken()) != JsonToken.end_document) {

                if (token == JsonToken.field) {
                    field = reader.getFieldName();
                    result.put(field, new ArrayList<>());
                }
                else if (token == JsonToken.value) {
                    JsonValue tokenValue = reader.getValue();
                    result.get(field).add(tokenValue);
                }
            }
            Assert.assertTrue(result.get("address").size() == 0);
            Assert.assertEquals(((JsonValue) result.get("mail").get(0) ).getValue(), Boolean.FALSE);
            Assert.assertEquals(((JsonValue) result.get("city").get(0) ).getValue(), "Afdracht");
            Assert.assertEquals(((JsonValue) result.get("street").get(0) ).getValue(), "Op de hoge bommel");
            Assert.assertEquals(((JsonValue) result.get("name").get(0) ).getValue(), "Jan Pietersen");
            Assert.assertEquals(((JsonValue) result.get("houseNumber").get(0) ).getValue(), 231l);
            Assert.assertEquals(result.get("scores").size(), 5);
        }
    }

    public void testJsonFile2() throws IOException {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("test2.json")) {
            JsonReader reader = new JsonReader(is, StandardCharsets.UTF_8);

//            parseWeatherLocation1(reader);
//            parseWeatherLocation2(reader);
        }
    }


}
