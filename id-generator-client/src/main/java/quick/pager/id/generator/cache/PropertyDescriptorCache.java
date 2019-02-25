package quick.pager.id.generator.cache;

import java.beans.PropertyDescriptor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JVM 内存数据
 *
 * @author siguiyang
 */
public class PropertyDescriptorCache {

    /**
     * class PropertyDescriptor 反射器的缓存
     */
    private static final Map<Class<?>, PropertyDescriptor> PROPERTY_DESCRIPTOR_MAP = new ConcurrentHashMap<>();


    /**
     * 获取值
     *
     * @param clazz class对象
     * @return 属性反射器
     */
    public static PropertyDescriptor get(Class<?> clazz) {
        return PROPERTY_DESCRIPTOR_MAP.get(clazz);
    }

    /**
     * 设值
     *
     * @param clazz              class对象
     * @param propertyDescriptor 属性反射器
     */
    public static void put(Class<?> clazz, PropertyDescriptor propertyDescriptor) {
        PROPERTY_DESCRIPTOR_MAP.putIfAbsent(clazz, propertyDescriptor);
    }

    /**
     * 是否存在
     *
     * @param clazz class对象
     * @return true 存在 | false 不存在
     */
    public static boolean containsKey(Class<?> clazz) {
        return PROPERTY_DESCRIPTOR_MAP.containsKey(clazz);
    }

    public static Map<Class<?>, PropertyDescriptor> getPropertyDescriptorMap() {
        return PROPERTY_DESCRIPTOR_MAP;
    }

    /**
     * 提供对外暴露清除缓存的方法
     */
    public static void clear() {
        PROPERTY_DESCRIPTOR_MAP.clear();
    }
}
