package quick.pager.id.generator.resp;

import java.io.Serializable;
import lombok.Data;
import lombok.NonNull;

@Data
public class Response<T> implements Serializable {

    private int code = 200;

    @NonNull
    private String message = "操作成功";

    private T data;

    private long timestamp = System.currentTimeMillis();

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(T data) {
        this.data = data;
    }
}
