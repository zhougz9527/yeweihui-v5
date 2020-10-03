package com.yeweihui.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: baowp
 * Date: 2/17/14
 * Time: 7:32 PM
 */
public class BeanUtil {

    private static String GET = "get";
    private static Build extendBuild = new Build() {
        public boolean check(Map.Entry<?, Entry<?, Class<?>>> entry) {
            return entry.getValue().getKey() != null;
        }
    };
    private static Build extendAllBuild = new Build();


    public static <T> List<T> extend(Class<T> clazz,List<?> list2){
        List<T> list=new ArrayList<>();
        for (Object o:list2) {
            list.add(extendz(clazz,o));
        }
        return list;
    }
    /**
     * inherit not null properties from parent(s) to self
     *
     * @since 1.0
     */
    public static <T> T extend(T self, Object... parent) {
        for (Object obj : parent) {
            inherit(self, obj, extendBuild);
        }
        return self;
    }

    /**
     * @return new instance inherit parents' not null properties
     * @since 1.0
     */
    public static <T> T extendz(Class<T> clazz, Object... parent) {
        T newInstance = null;
        try {
            newInstance = clazz.newInstance();
        } catch (Exception e) {
        }
        extend(newInstance, parent);
        return newInstance;
    }

    /**
     * inherit all properties from parent(s) to self
     *
     * @since 1.0
     */
    public static <T> T extendAll(T self, Object... parent) {
        for (Object obj : parent) {
            inherit(self, obj, extendAllBuild);
        }
        return self;
    }

    private static void inherit(Object self, Object parent, Build build) {
        Map<String, Entry<?, Class<?>>> map = new HashMap<String, Entry<?, Class<?>>>();
        for (Method method : parent.getClass().getMethods()) {
            String name = method.getName();
            if (name.startsWith(GET)) {
                try {
                    map.put(getToSet(name),
                            new Entry<Object, Class<?>>(method.invoke(parent),
                                    method.getReturnType()));
                } catch (Exception e) {
                }
            }
        }
        for (Map.Entry<String, Entry<?, Class<?>>> entry : map.entrySet()) {
            if (!build.check(entry))
                continue;
            String key = build.getKey(entry);
            Entry<?, Class<?>> value = build.getValue(entry);
            try {
                Method method = self.getClass()
                        .getMethod(key, value.getValue());
                method.invoke(self, value.getKey());
            } catch (Exception e) {
            }
        }
    }

    private static String getToSet(String getName) {
        return new StringBuilder(getName).deleteCharAt(0).insert(0, 's')
                .toString();
    }

    private static String getToProperty(String getName) {
        if (getName.length() < 4)
            throw new IllegalArgumentException("getName shorter than four");
        return getName.substring(3, 4).toLowerCase() + getName.substring(4);
    }

    /**
     * bean属性与值放到map键值对
     *
     * @param bean
     * @return
     */
    public static Map<String, Object> bean2map(Object bean) {
        if (bean == null)
            return null;
        else if (bean instanceof Map) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) bean).entrySet()) {
                if (entry.getKey() != null)
                    map.put(entry.getKey().toString(), entry.getValue());
            }
            return map;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        for (Method method : bean.getClass().getMethods()) {
            String getName = method.getName();
            if (!getName.startsWith(GET) || getName.equals("getClass")) {
                continue;
            }
            try {
                Object value = method.invoke(bean);
                if (value != null) {
                    map.put(getToProperty(getName), value);
                }
            } catch (Exception e) {
            }
        }
        return map;
    }

    public static String serialize(Object bean) {
        if (bean == null)
            return null;

        List<String> list = new ArrayList<String>();
        for (Method method : bean.getClass().getMethods()) {
            String getName = method.getName();
            if (!getName.startsWith(GET) || getName.equals("getClass")) {
                continue;
            }
            try {
                Object value = method.invoke(bean);
                if (value != null) {
                    list.add(getToProperty(getName) + '=' + value);
                }
            } catch (Exception e) {
            }
        }
        return StringUtils.join(list, '&');
    }

    private static class Build {
        protected boolean check(Map.Entry<?, Entry<?, Class<?>>> entry) {
            return true;
        }

        protected String getKey(Map.Entry<?, Entry<?, Class<?>>> entry) {
            return (String) entry.getKey();
        }

        protected Entry<?, Class<?>> getValue(
                Map.Entry<?, Entry<?, Class<?>>> entry) {
            return entry.getValue();
        }
    }

    private static class Entry<K, V> {

        private K key;
        private V value;

        public Entry(K k, V v) {
            key = k;
            value = v;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}

