package quick.pager.id.generator.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quick.pager.id.generator.demo.jpa.PersonRepository;
import quick.pager.id.generator.demo.model.Person;

@Service
public class JPATestService {

    @Autowired
    private PersonRepository personRepository;

    public void insert() {
        Person person = new Person();
//        person.setId(1L);
        person.setUsername("test");
        person.setAge(20);
        person.setGender(false);
        personRepository.save(person);

        Person p = personRepository.findById(person.getId()).get();

        System.out.println(p);
    }
}
