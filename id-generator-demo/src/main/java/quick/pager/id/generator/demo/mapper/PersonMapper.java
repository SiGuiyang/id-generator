package quick.pager.id.generator.demo.mapper;

import quick.pager.id.generator.demo.model.Person;

public interface PersonMapper {

    Person selectByPrimaryKey(Long id);

    int insertSelective(Person person);

    int insert();

    int updateByPrimaryKeySelective(Person person);
}
