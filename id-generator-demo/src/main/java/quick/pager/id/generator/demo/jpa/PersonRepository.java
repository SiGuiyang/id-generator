package quick.pager.id.generator.demo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quick.pager.id.generator.demo.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
