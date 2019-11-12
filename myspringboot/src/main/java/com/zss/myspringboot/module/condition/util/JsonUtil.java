//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zss.myspringboot.module.condition.util;


import com.zss.myspringboot.module.condition.jackson.CustomDateFormat;
import com.zss.myspringboot.module.condition.jackson.ExtendBeanDeserializerFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.deser.StdDeserializerProvider;
import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonUtil {
    private static final String JSON_NULL = "null";
    private static final String JSON_UNDEFINED = "undefined";
    private static final String JSON_EMPTY = "{}";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static TypeFactory typeFactory;

    public JsonUtil() {
    }

    public static String encode(Object object) {
        if (object == null) {
            return null;
        } else {
            try {
                mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); //将对象中时间格式转化
                return mapper.writeValueAsString(object);
            } catch (JsonGenerationException var2) {
                var2.printStackTrace();
            } catch (JsonMappingException var3) {
                var3.printStackTrace();
            } catch (IOException var4) {
                var4.printStackTrace();
            }

            return null;
        }
    }


    public static <T> T decode(String content, Class<T> valueType) {
        if (!StringUtils.hasText(content)) {
            return null;
        } else {
            content = prepareContent(content);

            try {
                return mapper.readValue(content, valueType);
            } catch (JsonParseException var3) {
                var3.printStackTrace();
            } catch (JsonMappingException var4) {
                var4.printStackTrace();
            } catch (IOException var5) {
                var5.printStackTrace();
            }

            return null;
        }
    }

    public  static  <T> T  getjson(String str,Class<T> value)
    {
       // JSONArray array= new JSONArray(json);
        return  null;
    }









    public static <T> T decode(Object content, Class<T> valueType) {
        return decode(encode(content), valueType);
    }

    public static <T> T decode(String content, Class<? extends Collection> collectionType, Class<?> elementType) {
        Collection empty = new ArrayList();
        if (!StringUtils.hasText(content)) {
            return null;//return empty;//cww:抄过来的代码，不知道为什么不能这么返回，而原来的可以。
        } else {
            content = prepareContent(content);

            try {
                return mapper.readValue(content, typeFactory.constructCollectionType(collectionType, elementType));
            } catch (JsonParseException var5) {
                var5.printStackTrace();
            } catch (JsonMappingException var6) {
                var6.printStackTrace();
            } catch (IOException var7) {
                var7.printStackTrace();
            }

            return null;//return empty;//cww:抄过来的代码，不知道为什么不能这么返回，而原来的可以。
        }
    }

    public static <T> T decode(String content, Class<? extends Map> mapType, Class<?> keyType, Class<?> valueType) {
        Map empty = new HashMap();
        if (!StringUtils.hasText(content)) {
            return null;//return empty;//cww:抄过来的代码，不知道为什么不能这么返回，而原来的可以。
        } else {
            content = prepareContent(content);

            try {
                return mapper.readValue(content, mapper.getTypeFactory().constructMapType(mapType, keyType, valueType));
            } catch (JsonParseException var6) {
                var6.printStackTrace();
            } catch (JsonMappingException var7) {
                var7.printStackTrace();
            } catch (IOException var8) {
                var8.printStackTrace();
            }

            return null;//return empty;//cww:抄过来的代码，不知道为什么不能这么返回，而原来的可以。
        }
    }

    public static <T> T decode(String content, String property, Class<T> valueType) {
        String json = parser(content, property);
        return decode(json, valueType);
    }

    public static <T> T decode(String content, String property, Class<? extends Collection> collectionType, Class<?> elementType) {
        String json = parser(content, property);
        return decode(json, collectionType, elementType);
    }

    public static String parser(String content, String property) {
        String[] values = parserList(content, property);
        return values != null && values.length >= 0 ? values[0] : null;
    }

    public static String[] parserList(String content, String... properties) {
        if (StringUtils.hasText(content) && properties != null && properties.length != 0) {
            String[] values = new String[properties.length];
            Map map = null;

            try {
                map = (Map)mapper.readValue(content, typeFactory.constructMapType(HashMap.class, String.class, Object.class));

                for(int i = 0; i < properties.length; ++i) {
                    String property = properties[i];
                    Object value = PropertyUtils.getProperty(map, property);
                    if (value instanceof Map || value instanceof Collection || value instanceof Set) {
                        value = encode(value);
                    }

                    values[i] = (String)value;
                }
            } catch (JsonParseException var7) {
                var7.printStackTrace();
            } catch (JsonMappingException var8) {
                var8.printStackTrace();
            } catch (IOException var9) {
                var9.printStackTrace();
            } catch (IllegalAccessException var10) {
                var10.printStackTrace();
            } catch (InvocationTargetException var11) {
                var11.printStackTrace();
            } catch (NoSuchMethodException var12) {
                var12.printStackTrace();
            }

            return values;
        } else {
            return null;
        }
    }

    public static String prepareContent(String content) {
        if (StringUtils.hasText(content) && content.indexOf(":\"null\"") > -1) {
            content = content.replaceAll(":\"null\"", ":null");
        }

        if (StringUtils.hasText(content) && content.indexOf(":\"undefined\"") > -1) {
            content = content.replaceAll(":\"undefined\"", ":null");
        }

        return content;
    }

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }

    public static boolean isBlank(String content) {
        return !StringUtils.hasText(content) || "null".equalsIgnoreCase(content) || "{}".equalsIgnoreCase(content) || "undefined".equals(content);
    }

    public static boolean isJson(String content) {
        if (!StringUtils.hasText(content)) {
            return false;
        } else {
            String first = String.valueOf(content.charAt(0));
            return JsonToken.START_OBJECT.asString().equals(first) || JsonToken.START_ARRAY.asString().equals(first);
        }
    }

    public static boolean isArrayJson(String content) {
        if (!StringUtils.hasText(content)) {
            return false;
        } else {
            String first = String.valueOf(content.charAt(0));
            return JsonToken.START_ARRAY.asString().equals(first);
        }
    }

    public static boolean isObjectJson(String content) {
        if (!StringUtils.hasText(content)) {
            return false;
        } else {
            String first = String.valueOf(content.charAt(0));
            return JsonToken.START_OBJECT.asString().equals(first);
        }
    }

    static {
        typeFactory = mapper.getTypeFactory();
        mapper.setDateFormat(CustomDateFormat.getDateFormat());
        mapper.getJsonFactory().configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        StdDeserializerProvider deserializerProvider = new StdDeserializerProvider(new ExtendBeanDeserializerFactory());
        mapper.setDeserializerProvider(deserializerProvider);
    }



}
