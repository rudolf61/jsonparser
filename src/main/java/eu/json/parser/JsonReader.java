package eu.json.parser;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Stack;

/**
 * Low level Json parser. It recognizes token and it only maintains the bare minimum that is required for
 * parsers that make use of this parser.
 * <p>
 *     It's resource usage is kept at a minimum. The implementation is very small and understandable just by looking at the code.
 * </p>
 * The JsonReader makes use of {@link JsonValue}. Jsonvalue can contain all posible elementary Json value types.
 */
public class JsonReader {

    public JsonReader(Reader rdr) {
        this.reader = new PushbackReader(rdr);
        tokens = new Stack<>();
    }

    public JsonReader(InputStream is, Charset charset) {
        this(new InputStreamReader(is, charset));
    }

    public JsonReader(String jsonString) {
        this(new StringReader(jsonString));
    }

    public JsonToken nextToken() throws IOException {
        if (!started) {
            started = true;
            return token = JsonToken.start_document;
        }
        if (end_document) {
            return token = JsonToken.end_document;
        }

        int i = reader.read();
        if (i == -1) {
            end_document = true;
            reader.close();
            return token = JsonToken.end_document;
        }

        char c = (char) i;

        token = null;
        switch (c) {
            case ' ':
            case '\t':
            case '\n':
            case '\r':
                token = JsonToken.ignore;
                break;
            case ':':
                if (fieldName == null) {
                    throw new JsonException("Field name expected");
                }
                token = JsonToken.ignore;
                break;
            case ',':
                if (!isArray()) {
                    fieldName = null;
                }
                token = JsonToken.ignore;
                break;
            case '[':
                token = JsonToken.start_array;
                tokens.push(token);
                break;
            case '{':
                token = JsonToken.start_object;
                tokens.push(token);
                fieldName = null;
                break;
            case ']':
                stackToken = tokens.pop();
                if (stackToken != JsonToken.start_array) {
                    throw new JsonException("Expected start_array before end_array");
                }
                token = JsonToken.end_array;
                break;
            case '}':
                stackToken = tokens.pop();
                if (stackToken != JsonToken.start_object) {
                    throw new JsonException("Expected start_object before end_object");
                }
                token = JsonToken.end_object;
                break;
            case '\"':
                if (fieldName == null) {
                    fieldName = parseString(c);
                    token = JsonToken.field;
                }
                else {
                    value = new JsonValue(parseString(c));
                    token = JsonToken.value;
                }
                break;
            case 'n':
                readNull(c);
                value=JsonValue.NULL_VALUE;
                token = JsonToken.value;
                break;
            case 't':
            case 'f':
                boolean b = parseBoolean(c);
                token = JsonToken.value;
                value = new JsonValue(b);
                break;
            default:
                Number number = parseNumber(c);
                token = JsonToken.value;
                if (number instanceof Double) {
                    value = new JsonValue((Double) number);
                }
                else {
                    value = new JsonValue((Long) number);
                }
        }

        return token;
    }

    private void readNull(char c) throws IOException {
        char[] nullval = new char[4];
        nullval[0] = c;
        for (int i = 1; i < 4; i++) {
            nullval[i] = (char) reader.read();
        }

        if (nullval[0] == 'n' && nullval[1] == 'u' && nullval[2] == 'l' && nullval[3] == 'l') {
            ;
        }
        else {
            throw new JsonException("Expected null value");
        }
    }

    private boolean parseBoolean(char c) throws IOException {
        char[] boolVal = new char[5];
        boolVal[0] = c;

        int uc = c == 't' ? 4 : 5;
        for (int i = 1; i < uc; i++) {
            boolVal[i] = (char) reader.read();
        }

        if (boolVal[0] == 't' && boolVal[1] == 'r' && boolVal[1] == 'u' && boolVal[1] == 'e') {
            return true;
        }
        if (boolVal[0] == 'f' && boolVal[1] == 'a' && boolVal[2] == 'l' && boolVal[3] == 's' && boolVal[4] == 'e') {
            return false;
        }

        throw new JsonException("Expected a boolean value, but found " + new String(boolVal));

    }

    private Number parseNumber(char c) throws IOException {
        StringBuilder sb = new StringBuilder().append(c);

        int i = reader.read();
        char ch = (char) i;

        while (Character.isDigit(ch) || ch == '.')
        {
            sb.append(ch);
            i = reader.read();
            ch = (char) i;
        }

        String v = sb.toString();
        Number number = null;

        if (v.indexOf('.') > -1) {
            number = Double.valueOf(v);
        }
        else {
            number = Long.valueOf(v);
        }

        reader.unread(i);

        return number;
    }


    private String parseString(char c) throws IOException {
        StringBuilder sb = new StringBuilder();

        char nc = (char) reader.read();
        while (nc != '\"') {
            sb.append(nc);
            nc = (char) reader.read();
        }

        if (nc != '\"') {
            throw new JsonException("Error reading String value " + sb.toString());
        }

        return sb.toString();

    }

    public String getFieldName() {
        return fieldName;
    }

    public JsonValue getValue() {
        return value;
    }

    public JsonToken getToken() {
        return token;
    }

    public boolean isArray() {
        return !tokens.empty() && tokens.peek() == JsonToken.start_array;
    }

    public boolean isObject() {
        return !tokens.empty() && tokens.peek() == JsonToken.start_object;
    }

    /**
     * Zero based
     *
     * @return
     */
    public int getLevel() {
        return tokens.size();
    }


    private PushbackReader reader;
    private String fieldName;
    private JsonValue value;
    private JsonToken token;
    private boolean started = false;
    private boolean end_document = false;
    private Stack<JsonToken> tokens;
    private JsonToken stackToken;

}
