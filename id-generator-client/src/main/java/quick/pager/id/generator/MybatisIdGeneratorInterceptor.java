package quick.pager.id.generator;

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
import quick.pager.id.generator.utils.ClassUtils;
import quick.pager.id.generator.utils.GeneratorUtils;

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
public class MybatisIdGeneratorInterceptor implements Interceptor {


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

            // 基本数据类型及基本数据类型数组剔除
            if (ClassUtils.isPrimitiveOrWrapper(parameterObject.getClass()) || !GeneratorUtils.isAvailable(parameterObject)) {
                return invocation.proceed();
            }

            // 如果是插入操作执行如下逻辑
            if (SqlCommandType.INSERT == mappedStatement.getSqlCommandType()) {

                GeneratorUtils.loadAllClassWidthIdGenerator(parameterObject);

                GeneratorUtils.requestIdGeneratorAndSetValue(parameterObject);

                return executor.update(mappedStatement, parameterObject);
            }

        }

        return invocation.proceed();
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        globalBizName = properties.getProperty("globalBizName");
    }

    public String getGlobalBizName() {
        return globalBizName;
    }

    public void setGlobalBizName(String globalBizName) {
        this.globalBizName = globalBizName;
    }
}
