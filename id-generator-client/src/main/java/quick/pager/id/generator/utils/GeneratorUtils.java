package quick.pager.id.generator.utils;

import com.alibaba.fastjson.JSON;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Map;
import quick.pager.id.generator.Result;
import quick.pager.id.generator.annotation.IdGenerator;
import quick.pager.id.generator.cache.GeneratorMapCache;
import quick.pager.id.generator.exception.IdGeneratorException;
import quick.pager.id.generator.meta.FieldMeta;

/**
 * ID 工具
 *
 * @author siguiyang
 */
public class GeneratorUtils {

    /**
     * 加载所有的clazz对象如缓存
     */
    public static void loadAllClassWidthIdGenerator(Object entity) throws IntrospectionException {

        Class<?> clazz = entity.getClass();

        if (!GeneratorMapCache.FIELD_META_MAP.containsKey(clazz) && !GeneratorMapCache.PROPERTY_DESCRIPTOR_MAP.containsKey(clazz)) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(IdGenerator.class)) {
                    GeneratorMapCache.FIELD_META_MAP.put(clazz, new FieldMeta(field, field.getAnnotation(IdGenerator.class)));
                    GeneratorMapCache.PROPERTY_DESCRIPTOR_MAP.put(clazz, new PropertyDescriptor(field.getName(), clazz));
                    break;
                }
            }
        }
    }

    /**
     * 向Id生成器服务请求，或者当前生成的Id值<br />
     * 并使用反射方式给字段设置当前的Id值
     */
    public static Serializable requestIdGeneratorAndSetValue(Object entity) throws InvocationTargetException, IllegalAccessException {
        FieldMeta fieldMeta = GeneratorMapCache.FIELD_META_MAP.get(entity.getClass());
        PropertyDescriptor propertyDescriptor = GeneratorMapCache.PROPERTY_DESCRIPTOR_MAP.get(entity.getClass());

        if (null == fieldMeta || null == propertyDescriptor) {
            throw new IdGeneratorException("数据缓存异常，请检查加入缓存的业务");
        }

        // 获取请求服务地址
        String url = getRealURL(fieldMeta);
        // 请求id生成器服务
        String result = HttpClientUtils.doPost(url, Collections.emptyMap());
        if (StringUtils.hasLength(result)) {
            Result response = JSON.parseObject(result, Result.class);
            if (null == response || 200 != response.getCode()) {
                throw new IdGeneratorException("获取Id生成器服务发生异常 biz_name = " + fieldMeta.getBizName());
            }

            // 获得用于写入属性值的方法
            Method method = propertyDescriptor.getWriteMethod();
            method.invoke(entity, response.getData());
            return response.getData();
        } else {
            throw new IdGeneratorException("获取Id生成器服务发生异常 biz_name = " + fieldMeta.getBizName());
        }

    }


    private static String getRealURL(FieldMeta fieldMeta) {
        String url = null;
        // 是否存在批量处理
        if (fieldMeta.isSingleOperation()) {
            String requestUrl = System.getProperty("id.generator.requestUrl");
            Assert.notNull(requestUrl, "请配置 System.setProperty('id.generator.requestUrl','requestUrl') 设置请求Id生成器的服务");
            url = requestUrl + "/id/generator/" + fieldMeta.getBizName();
        } else {
            // TODO 第一版暂不支持批量处理
        }
        return url;
    }

    /**
     * 是否可用的
     *
     * @param parameterObject mybatis insert 操作的入参
     */
    public static boolean isAvailable(Object parameterObject) {
        Class clazz = parameterObject.getClass();
        return !(parameterObject instanceof Map || clazz.isInterface() || clazz.isEnum() || Modifier.isAbstract(clazz.getModifiers()));
    }
}
