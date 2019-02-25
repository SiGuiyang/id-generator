package quick.pager.id.generator;

import com.alibaba.fastjson.JSON;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Map;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.util.Properties;
import quick.pager.id.generator.annotation.IdGenerator;
import quick.pager.id.generator.cache.FieldMetaCache;
import quick.pager.id.generator.cache.PropertyDescriptorCache;
import quick.pager.id.generator.exception.IdGeneratorException;
import quick.pager.id.generator.meta.FieldMeta;
import quick.pager.id.generator.resp.Response;
import quick.pager.id.generator.utils.ClassUtils;
import quick.pager.id.generator.utils.HttpClientUtils;
import quick.pager.id.generator.utils.StringUtils;

/**
 * Mybatis 拦截器<br />
 * 此拦截器不支持联合主键方式
 *
 * @author siguiyang
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
        }
)
public class IdGeneratorInterceptor implements Interceptor {


    /**
     * 请求访问的地址
     */
    private String requestUrl;

    /**
     * 全局的请求biz_name 号段
     */
    private String globalBizName;


    public Object intercept(Invocation invocation) throws Throwable {

        Object[] args = invocation.getArgs();

        if (invocation.getTarget() instanceof Executor) {
            Executor executor = (Executor) invocation.getTarget();
            MappedStatement mappedStatement = (MappedStatement) args[0];
            Object objectParams = args[1];

            // 插入没有参数，直接通过，下面逻辑pass
            if (null == objectParams) {
                return invocation.proceed();
            }

            BoundSql boundSql = mappedStatement.getBoundSql(objectParams);
            Object parameterObject = boundSql.getParameterObject();

            Class clazz = parameterObject.getClass();

            // 基本数据类型及基本数据类型数组剔除
            if (ClassUtils.isPrimitiveOrWrapper(clazz) || !isAvailable(parameterObject)) {
                return invocation.proceed();
            }

            // 如果是插入操作执行如下逻辑
            if (SqlCommandType.INSERT == mappedStatement.getSqlCommandType()) {

                loadAllClazzWidthIdGenerator(clazz);

                requestIdGeneratorAndSetValue(clazz, parameterObject);

                return executor.update(mappedStatement, parameterObject);
            }

        }

        return invocation.proceed();
    }

    /**
     * 向Id生成器服务请求，或者当前生成的Id值<br />
     * 并使用反射方式给字段设置当前的Id值
     */
    private void requestIdGeneratorAndSetValue(Class clazz, Object parameterObject) throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        FieldMeta fieldMeta = FieldMetaCache.get(clazz);
        PropertyDescriptor propertyDescriptor = PropertyDescriptorCache.get(clazz);

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
            method.invoke(parameterObject, response.getData());
        } else {
            throw new IdGeneratorException("获取Id生成器服务发生异常 biz_name = " + fieldMeta.getBizName());
        }

    }

    /**
     * 真实请求请求服务地址
     *
     * @param fieldMeta fieldMeta 存储
     */
    private String getRealURL(FieldMeta fieldMeta) {
        String url = null;
        // 是否存在批量处理
        if (fieldMeta.isSingleOperation()) {
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
    private boolean isAvailable(Object parameterObject) {
        Class clazz = parameterObject.getClass();
        return !(parameterObject instanceof Map || clazz.isInterface() || clazz.isEnum() || Modifier.isAbstract(clazz.getModifiers()));
    }

    /**
     * 加载所有注有 IdGenerator 注解的class对象到缓存中
     */
    private void loadAllClazzWidthIdGenerator(Class<?> clazz) throws IntrospectionException {

        if (!FieldMetaCache.containsKey(clazz) && !PropertyDescriptorCache.containsKey(clazz)) {

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(IdGenerator.class)) {

                    FieldMetaCache.put(clazz, new FieldMeta(field, field.getAnnotation(IdGenerator.class)));

                    PropertyDescriptorCache.put(clazz, new PropertyDescriptor(field.getName(), clazz));

                    break;
                }
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        requestUrl = properties.getProperty("requestUrl");
        globalBizName = properties.getProperty("globalBizName");
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getGlobalBizName() {
        return globalBizName;
    }

    public void setGlobalBizName(String globalBizName) {
        this.globalBizName = globalBizName;
    }
}
