package quick.pager.id.generator.meta;

import java.lang.reflect.Field;
import quick.pager.id.generator.annotation.IdGenerator;

public interface IMeta {

    Field getField();

    IdGenerator getIdGenerator();

    String getBizName();
}
