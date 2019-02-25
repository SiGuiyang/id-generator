package quick.pager.id.generator.demo.model;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;
import quick.pager.id.generator.annotation.IdGenerator;

@Data
@ToString
public class Person implements Serializable {

    @IdGenerator(value = "test")
    private Long id;

    private String username;

    private Integer age;

    private Boolean gender;

}
