package quick.pager.id.generator;

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import quick.pager.id.generator.exception.IdGeneratorException;
import quick.pager.id.generator.utils.GeneratorUtils;

/**
 * ID生成策略
 *
 * @author siguiyang
 */
public class HibernateIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        try {
            GeneratorUtils.loadAllClassWidthIdGenerator(object);
            return GeneratorUtils.requestIdGeneratorAndSetValue(object);
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new IdGeneratorException("Id 生成器策略异常");
    }
}
