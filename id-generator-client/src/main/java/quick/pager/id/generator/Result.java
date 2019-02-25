package quick.pager.id.generator;

import java.io.Serializable;
import lombok.Data;

@Data
public class Result implements Serializable {

    private int code = 200;

    private String message = "操作成功";

    private Long data;

    private long timestamp = System.currentTimeMillis();

}
