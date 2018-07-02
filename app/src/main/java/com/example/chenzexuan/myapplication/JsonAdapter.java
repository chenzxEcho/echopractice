package com.example.chenzexuan.myapplication;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by molikto on 03/19/16. */
public abstract class JsonAdapter<T> {
  public static JsonFactory jf = null;

  public static final JsonFactory JSON_FACTORY() {
    if (jf == null) {
      synchronized (JsonAdapter.class) {
        if (jf == null) {
          jf = new JsonFactory();
        }
      }
    }
    return jf;
  }

  private JsonAdapter<List<T>> aa = null;

  public JsonAdapter<List<T>> ARRAY_ADAPTER() {
    if (aa == null) aa = ARRAY_ADAPTER(this);
    return aa;
  }

  private JsonAdapter<Map<String, T>> ma = null;

  public JsonAdapter<Map<String, T>> MAP_ADAPTER() {
    if (ma == null) ma = MAP_ADAPTER(this);
    return ma;
  }
  /** input can be json null, output can also be null */
  public abstract T parse(JsonParser jp) throws IOException;

  /**
   * null must be transformed to json null, so in some cases the whole code will take this shortcut
   */
  public abstract void serialize(T t, JsonGenerator jg, boolean writeStartEndObject)
      throws IOException;

  /** statics */

  public static final JsonAdapter<Double> DOUBLE_ADAPTER =
      new JsonAdapter<Double>() {
        @Override
        public Double parse(JsonParser jp) throws IOException {
          return jp.getValueAsDouble();
        }

        @Override
        public void serialize(Double s, JsonGenerator jg, boolean writeStartEndObject)
            throws IOException {
          jg.writeNumber(s);
        }
      };

  public static final JsonAdapter<Integer> INT_ADAPTER =
      new JsonAdapter<Integer>() {
        @Override
        public Integer parse(JsonParser jp) throws IOException {
          return jp.getValueAsInt();
        }

        @Override
        public void serialize(Integer s, JsonGenerator jg, boolean writeStartEndObject)
            throws IOException {
          jg.writeNumber(s);
        }
      };

  public static final JsonAdapter<String> STRING_ADAPTER =
      new JsonAdapter<String>() {
        @Override
        public String parse(JsonParser jp) throws IOException {
          if (jp.getCurrentToken() == JsonToken.VALUE_NULL) {
            return null;
          } else {
            return jp.getValueAsString();
          }
        }

        @Override
        public void serialize(String s, JsonGenerator jg, boolean writeStartEndObject)
            throws IOException {
          jg.writeString(s);
        }
      };

  public static <T> JsonAdapter<Map<String, T>> MAP_ADAPTER(JsonAdapter<T> from) {
    return new JsonAdapter<Map<String, T>>() {
      @Override
      public Map<String, T> parse(JsonParser jp) throws IOException {
        return parseMap(jp, from);
      }

      @Override
      public void serialize(Map<String, T> ts, JsonGenerator jg, boolean writeStartEndObject)
          throws IOException {
        serializeMap(ts, jg, from);
      }

      @Override
      public Map<String, T> parse(byte[] bytes) throws IOException {
        if (bytes == null) {
          return null;
        } else if (bytes.length == 2) {
          return new HashMap<>();
        } else {
          return super.parse(bytes);
        }
      }
    };
  }

  public static <T> JsonAdapter<List<T>> ARRAY_ADAPTER(JsonAdapter<T> from) {
    return new JsonAdapter<List<T>>() {
      @Override
      public List<T> parse(JsonParser jp) throws IOException {
        return parseArray(jp, from);
      }

      @Override
      public void serialize(List<T> ts, JsonGenerator jg, boolean writeStartEndObject)
          throws IOException {
        serializeArray(ts, jg, from);
      }

      @Override
      public List<T> parse(byte[] bytes) throws IOException {
        if (bytes == null) {
          return null;
        } else if (bytes.length == 2) {
          return new ArrayList<>(0);
        } else {
          return super.parse(bytes);
        }
      }
    };
  }

  /** helper */
  protected static <K> void serializeArray(
      List<K> arr, JsonGenerator jsonGenerator, JsonAdapter<K> adapter) throws IOException {
    if (arr == null) {
      jsonGenerator.writeNull();
    } else {
      jsonGenerator.writeStartArray();
      for (K element1 : arr) {
        if (element1 != null) {
          adapter.serialize(element1, jsonGenerator, true);
        }
      }
      jsonGenerator.writeEndArray();
    }
  }

  protected static <K> void serializeMap(
      Map<String, K> arr, JsonGenerator jsonGenerator, JsonAdapter<K> adapter) throws IOException {
    if (arr == null) {
      jsonGenerator.writeNull();
    } else {
      jsonGenerator.writeStartObject();
      for (Map.Entry<String, K> element1 : arr.entrySet()) {
        jsonGenerator.writeFieldName(element1.getKey());
        adapter.serialize(element1.getValue(), jsonGenerator, true);
      }
      jsonGenerator.writeEndObject();
    }
  }

  protected static <K> List<K> parseArray(JsonParser jsonParser, JsonAdapter<K> adapter)
      throws IOException {
    if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
      ArrayList<K> collection1 = new ArrayList<>(0);
      while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
        K value1;
        value1 = adapter.parse(jsonParser);
        if (value1 != null) collection1.add(value1);
      }
      return collection1;
    } else {
      return null;
    }
  }

  protected static <K> HashMap<String, K> parseMap(JsonParser jsonParser, JsonAdapter<K> adapter)
      throws IOException {
    if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
      HashMap<String, K> map1 = new HashMap<>();
      while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
        String key1 = jsonParser.getText();
        jsonParser.nextToken();
        map1.put(key1, adapter.parse(jsonParser));
      }
      return map1;
    } else {
      return null;
    }
  }

  public T parse(byte[] bytes) throws IOException {
    JsonParser jsonParser = JSON_FACTORY().createParser(bytes);
    jsonParser.nextToken();
    T t = parse(jsonParser);
    jsonParser.close();
    return t;
  }

  public T parse(InputStream is) throws IOException {
    JsonParser jsonParser = JSON_FACTORY().createParser(is);
    jsonParser.nextToken();
    T t = parse(jsonParser);
    jsonParser.close();
    return t;
  }

  public T parse(String jsonString) throws IOException {
    JsonParser jsonParser = JSON_FACTORY().createParser(jsonString);
    jsonParser.nextToken();
    T t = parse(jsonParser);
    jsonParser.close();
    return t;
  }

  //	public byte[] serialize(T object) throws IOException {
  //		ByteArrayOutputStream out = new ByteArrayOutputStream();
  //		JsonGenerator generator = JSON_FACTORY().createGenerator(out);
  //		serialize(object, generator, true);
  //		generator.close();
  //		return out.toByteArray();
  //	}

  public String serialize(T object) {
    StringWriter sw = new StringWriter();
    JsonGenerator jsonGenerator = null;
    try {
      jsonGenerator = JSON_FACTORY().createGenerator(sw);
      serialize(object, jsonGenerator, true);
      jsonGenerator.close();
    } catch (IOException e) {
//      App.me.logError(e);
    }
    return sw.toString();
  }

  /**
   * Serialize an object to an OutputStream.
   *
   * @param object The object to serialize.
   * @param os The OutputStream being written to.
   */
  public void serialize(T object, OutputStream os) {
    JsonGenerator jsonGenerator = null;
    try {
      jsonGenerator = JSON_FACTORY().createGenerator(os);
      serialize(object, jsonGenerator, true);
      jsonGenerator.close();
    } catch (IOException e) {
//      App.me.logError(e);
    }
  }
}
