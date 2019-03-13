package quick.pager.id.generator.meta;

import java.lang.reflect.Field;
import quick.pager.id.generator.annotation.IdGenerator;

public class FieldMeta implements IMeta {

    private Field field;

    private IdGenerator idGenerator;

    public FieldMeta(Field field, IdGenerator idGenerator) {
        this.field = field;
        this.idGenerator = idGenerator;
    }

    @Override
    public Field getField() {
        return this.field;
    }

    @Override
    public IdGenerator getIdGenerator() {
        return this.idGenerator;
    }

    @Override
    public String getBizName() {
        return this.idGenerator.value();
    }
}
