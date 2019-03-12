package quick.pager.id.generator.web;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class GeneratorContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        GeneratorContext.applicationContext = applicationContext;

    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return GeneratorContext.applicationContext.getBean(beanName, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return GeneratorContext.applicationContext.getBean(clazz);
    }
}
