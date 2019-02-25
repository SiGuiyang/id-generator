package quick.pager.id.generator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Id生成注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface IdGenerator {

    /**
     * biz_name 号段标识
     */
    String value() default "id";

    /**
     * 批量操作<br />
     * 默认不批量处理<br />
     * true 批量处理<br />
     * 如果操作的实体类中存在批量插入的操作，则必须将此属性设置为false
     */
    boolean singleOperation() default true;
}
