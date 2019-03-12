package quick.pager.id.generator.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quick.pager.id.generator.demo.mapper.PersonMapper;
import quick.pager.id.generator.demo.model.Person;

@Service
public class MybatisTestService {

    @Autowired
    private PersonMapper personMapper;

    public void insert() {
        Person person = new Person();
        person.setId(1L);
        person.setUsername("test");
        person.setAge(20);
        person.setGender(false);

        personMapper.insertSelective(person);
    }

    public void testSelectMybatis() {

        Person person = new Person();
//        person.setId(1L);
        person.setUsername("test");
        person.setAge(20);
        person.setGender(false);

        personMapper.insertSelective(person);

        Person selectPerson = personMapper.selectByPrimaryKey(person.getId());

        System.out.println(selectPerson);

    }
}
