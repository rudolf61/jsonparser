package eu.json.parser;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class JsonValue {
    public static final JsonValue NULL_VALUE = new JsonValue();
    private ByteBuffer bb;
    private Class<?> clz;
    private boolean nullValue;

    private JsonValue() {
        nullValue = true;
        clz = null;
    }


    public JsonValue(Long l) {
        if (l != null) {
            bb = ByteBuffer.allocate(Long.BYTES);
            bb.putLong(l);
            bb.flip();
            nullValue = false;
        } else {
            nullValue = true;
        }
        clz = Long.class;
    }

    public JsonValue(Integer i) {
        if (i != null) {
            bb = ByteBuffer.allocate(Integer.BYTES);
            bb.putLong(i);
            bb.flip();
            nullValue = false;
        } else {
            nullValue = true;
        }
        clz = Integer.class;
    }

    public JsonValue(Boolean bool) {
        if (bool != null) {
            bb = ByteBuffer.allocate(Byte.BYTES);
            bb.put((byte) (bool ? 1 : 0));
            bb.flip();
            nullValue = false;
        } else {
            nullValue = true;
        }
        clz = Boolean.class;

    }

    public JsonValue(String s) {
        if (s != null) {
            byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
            bb = ByteBuffer.allocate(bytes.length);
            bb.put(bytes);
            bb.flip();
            nullValue = false;
        } else {
            nullValue = true;
        }
        clz = String.class;
    }

    public JsonValue(Double d) {
        if (d != null) {
            bb = ByteBuffer.allocate(Double.BYTES);
            bb.putDouble(d);
            bb.flip();
            nullValue = false;
        } else {
            nullValue = true;
        }
        clz = Double.class;
    }

    public JsonValue(Float f) {
        if (f != null) {
            bb = ByteBuffer.allocate(Float.BYTES);
            bb.putDouble(f);
            bb.flip();
            nullValue = false;
        } else {
            nullValue = true;
        }
        clz = Float.class;
    }

    public Double getDoubleValue() {
        if (nullValue) return null;


        if (clz == Double.class) {
            double d = bb.getDouble();
            bb.rewind();
            return Double.valueOf(d);
        }
        if (clz == Float.class) {
            float f = bb.getFloat();
            bb.rewind();
            return Double.valueOf(f);
        }
        if (clz == Integer.class) {
            int i = bb.getInt();
            bb.rewind();
            return Double.valueOf(i);
        }
        if (clz == Long.class) {
            long l = bb.getLong();
            bb.rewind();
            return Double.valueOf(l);
        }
        throw new JsonException("Conversion error. Conversion for " + clz.getSimpleName() + " is not supported");
    }

    public Float getFloatValue() {
        if (nullValue) return null;

        if (clz == Float.class) {
            float f = bb.getFloat();
            bb.rewind();
            return Float.valueOf(f);
        }
        if (clz == Double.class) {
            double d = bb.getDouble();
            bb.rewind();
            return Float.valueOf((float) d);
        }
        if (clz == Integer.class) {
            int i = bb.getInt();
            bb.rewind();
            return Float.valueOf(i);
        }
        if (clz == Long.class) {
            long l = bb.getLong();
            bb.rewind();
            return Float.valueOf(l);
        }
        throw new JsonException("Conversion error. Conversion for " + clz.getSimpleName() + " is not supported");
    }

    public String getStringValue() {
        if (nullValue) return null;

        if (clz == String.class) {
            byte[] bytes = bb.array();
            bb.rewind();
            return new String(bytes, StandardCharsets.UTF_8);
        }
        if (clz == Integer.class) {
            int i = bb.getInt();
            bb.rewind();
            return String.valueOf(i);
        }
        if (clz == Double.class) {
            double d = bb.getDouble();
            bb.rewind();
            return String.valueOf(d);
        }
        if (clz == Boolean.class) {
            byte b = bb.get();
            bb.rewind();
            return String.valueOf(b == 0 ? Boolean.FALSE.toString() : Boolean.TRUE.toString());
        }
        if (clz == Long.class) {
            long l = bb.getLong();
            bb.rewind();
            return String.valueOf(l);
        }
        if (clz == Float.class) {
            float f = bb.getFloat();
            bb.rewind();
            return String.valueOf(f);
        }
        throw new JsonException("Conversion error. Conversion for " + clz.getSimpleName() + " is not supported");
    }


    public Integer getIntValue() {
        if (nullValue) return null;

        if (clz == Integer.class) {
            int i = bb.getInt();
            bb.rewind();
            return Integer.valueOf(i);
        }
        if (clz == Long.class) {
            long l = bb.getLong();
            bb.rewind();
            return Integer.valueOf((int) l);
        }
        if (clz == Double.class) {
            double d = bb.getDouble();
            bb.rewind();
            return Integer.valueOf((int) d);
        }
        if (clz == Float.class) {
            float f = bb.getFloat();
            bb.rewind();
            return Integer.valueOf((int) f);
        }
        throw new JsonException("Conversion error. Conversion for " + clz.getSimpleName() + " is not supported");
    }

    public Long getLongValue() {
        if (nullValue) return null;

        if (clz == Long.class) {
            long l = bb.getLong();
            bb.rewind();
            return Long.valueOf((int) l);
        }
        if (clz == Integer.class) {
            int i = bb.getInt();
            bb.rewind();
            return Long.valueOf(i);
        }
        if (clz == Double.class) {
            double d = bb.getDouble();
            bb.rewind();
            return Long.valueOf((long) d);
        }
        if (clz == Float.class) {
            float f = bb.getFloat();
            bb.rewind();
            return Long.valueOf((long) f);
        }

        throw new JsonException("Conversion error. Conversion for " + clz.getSimpleName() + " is not supported");
    }

    public Boolean getBooleanValue() {
        if (nullValue) return null;

        if (clz == Boolean.class) {
            byte b = bb.get();
            bb.rewind();
            return b == 0 ? Boolean.FALSE : Boolean.TRUE;
        }
        if (clz == String.class) {
            byte[] bytes = bb.array();
            bb.rewind();
            String boolVal = new String(bytes, StandardCharsets.UTF_8).toLowerCase();
            return Boolean.valueOf(boolVal);
        }
        if (clz == Integer.class) {
            int i = bb.getInt();
            bb.rewind();
            return i == 0 ? Boolean.FALSE : Boolean.TRUE;
        }
        if (clz == Long.class) {
            long l = bb.getLong();
            bb.rewind();
            return l == 0 ? Boolean.FALSE : Boolean.TRUE;
        }

        throw new JsonException("Conversion error. Conversion for " + clz.getSimpleName() + " is not supported");
    }

    public Object getValue() {
        if (nullValue) return null;

        if (clz == Boolean.class) {
            byte b = bb.get();
            bb.rewind();
            return b == 0 ? Boolean.FALSE : Boolean.TRUE;
        }
        if (clz == String.class) {
            byte[] bytes = bb.array();
            bb.rewind();
            String val = new String(bytes, StandardCharsets.UTF_8);
            return val;
        }
        if (clz == Integer.class) {
            int i = bb.getInt();
            bb.rewind();
            return Integer.valueOf(i);
        }
        if (clz == Long.class) {
            long l = bb.getLong();
            bb.rewind();
            return Long.valueOf(l);
        }
        if (clz == Double.class) {
            double d = bb.getDouble();
            bb.rewind();
            return Double.valueOf(d);
        }
        if (clz == Float.class) {
            float f = bb.getFloat();
            bb.rewind();
            return Float.valueOf(f);
        }

        return null;
    }

    @Override
    public String toString() {
        Object val = getValue();
        return  val == null ? "null" : val.toString();
    }
}