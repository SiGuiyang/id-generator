package quick.pager.id.generator.web;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import quick.pager.id.generator.annotation.IdGenerator;
import quick.pager.id.generator.cache.FieldMetaCache;
import quick.pager.id.generator.cache.PropertyDescriptorCache;
import quick.pager.id.generator.demo.model.Person;
import quick.pager.id.generator.exception.IdGeneratorException;
import quick.pager.id.generator.meta.FieldMeta;


//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoader() {

    }


    @Test
    public void testReflect() throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        Person person = new Person();
        person.setId(1L);
        person.setUsername("test");
        person.setAge(20);
        person.setGender(false);

        System.out.println(person);


        PropertyDescriptor propertyDescriptor = new PropertyDescriptor("age", person.getClass());

        Method writeMethod = propertyDescriptor.getWriteMethod();

        writeMethod.invoke(person, 2999);

        System.out.println(person);
    }


}
