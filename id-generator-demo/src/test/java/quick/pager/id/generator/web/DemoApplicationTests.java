package quick.pager.id.generator.web;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import quick.pager.id.generator.demo.DemoApplication;
import quick.pager.id.generator.demo.jpa.PersonRepository;
import quick.pager.id.generator.demo.model.Person;
import quick.pager.id.generator.demo.service.MybatisTestService;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class DemoApplicationTests {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MybatisTestService testService;

    @Test
    public void contextLoader() {
//        testService.insert();


        Person person = new Person();
        person.setId(100L);
        person.setUsername("test2222");
        person.setAge(20);
        person.setGender(false);

        personRepository.save(person);

        List<Person> all = personRepository.findAll();
        System.out.println(all);
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
