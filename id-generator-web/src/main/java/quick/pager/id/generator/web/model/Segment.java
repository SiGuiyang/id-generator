package quick.pager.id.generator.web.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Segment implements Serializable {

    private Long id;
    /**
     * 初始号段值
     */
    private Long segment;
    /**
     * 号段名称标识
     */
    private String bizName;
    /**
     * 描述
     */
    private String description;
    /**
     * 一次加载入缓存的数量
     */
    private Integer steps;
    /**
     * 加载Id生成器的方式
     * 0 JVM
     * 1 Redis
     */
    private Integer bizType;
    /**
     * 号段创建时间
     */
    private Date createTime;
    /**
     * 是否禁用
     * 0 未禁用
     * 1 禁用
     */
    private Boolean deleteStatus;

}
