package quick.pager.id.generator.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import quick.pager.id.generator.meta.FieldMeta;

public class FieldMetaCache {


    /**
     * class id字段缓存
     */
    private static final Map<Class<?>, FieldMeta> FIELD_META_MAP = new ConcurrentHashMap<>();
    /**
     * class id field批量集合缓存
     */
    private static final Map<Class<?>, List<FieldMeta>> FIELD_META_LIST_MAP = new ConcurrentHashMap<>();


    /**
     * 获取值
     *
     * @param clazz class对象
     * @return fieldMeta 存储
     */
    public static FieldMeta get(Class<?> clazz) {
        return FIELD_META_MAP.get(clazz);
    }

    /**
     * 设值
     *
     * @param clazz     class对象
     * @param fieldMeta fieldMeta 存储
     */
    public static void put(Class<?> clazz, FieldMeta fieldMeta) {
        FIELD_META_MAP.putIfAbsent(clazz, fieldMeta);
    }

    /**
     * 是否存在
     *
     * @param clazz class对象
     * @return true 存在 | false 不存在
     */
    public static boolean containsKey(Class<?> clazz) {
        return FIELD_META_MAP.containsKey(clazz);
    }

    /**
     * 提供对外暴露清除缓存的方法
     */
    public static void clear() {
        FIELD_META_MAP.clear();
        FIELD_META_LIST_MAP.clear();
    }

}
