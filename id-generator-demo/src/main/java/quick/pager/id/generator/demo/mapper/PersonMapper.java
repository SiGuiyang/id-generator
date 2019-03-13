package quick.pager.id.generator.demo.mapper;

import java.util.List;
import quick.pager.id.generator.demo.model.Person;

public interface PersonMapper {

    Person selectByPrimaryKey(Long id);

    int insertSelective(Person person);

    int batchInsert(List<Person> people);

    int insert();

    int updateByPrimaryKeySelective(Person person);
}
