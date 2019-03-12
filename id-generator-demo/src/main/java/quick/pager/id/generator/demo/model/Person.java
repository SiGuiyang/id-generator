package quick.pager.id.generator.demo.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import quick.pager.id.generator.annotation.IdGenerator;

@Data
@ToString
@Entity(name = "t_person")
public class Person implements Serializable {

    @IdGenerator(value = "test")
    @GeneratedValue(generator = "hibernateIdGenerator")
    @GenericGenerator(name = "hibernateIdGenerator", strategy = "quick.pager.id.generator.HibernateIdGenerator")
    @Id
    private Long id;

    private String username;

    private Integer age;

    private Boolean gender;

}
