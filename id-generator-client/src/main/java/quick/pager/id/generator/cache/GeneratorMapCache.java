package quick.pager.id.generator.cache;

import java.beans.PropertyDescriptor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Data;
import quick.pager.id.generator.meta.FieldMeta;

@Data
public class GeneratorMapCache {

    /**
     * class id字段缓存
     */
    public static final Map<Class<?>, FieldMeta> FIELD_META_MAP = new ConcurrentHashMap<>();

    /**
     * class PropertyDescriptor 反射器的缓存
     */
    public static final Map<Class<?>, PropertyDescriptor> PROPERTY_DESCRIPTOR_MAP = new ConcurrentHashMap<>();


    public static void clear() {
        FIELD_META_MAP.clear();
        PROPERTY_DESCRIPTOR_MAP.clear();
    }
}
