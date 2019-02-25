package quick.pager.id.generator.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class IdGeneratorException extends RuntimeException {

    public IdGeneratorException(String message) {
        super(message);
    }

    public IdGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdGeneratorException(Throwable cause) {
        super(cause);
    }

    protected IdGeneratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
