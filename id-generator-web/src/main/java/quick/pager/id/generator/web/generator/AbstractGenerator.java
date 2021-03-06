package quick.pager.id.generator.web.generator;

import java.util.concurrent.ExecutorService;
import lombok.Data;
import quick.pager.id.generator.web.service.GeneratorLoad;

@Data
public abstract class AbstractGenerator implements IGenerator {

    // biz_name 号段
    private String bizName;
    // 加载的步长
    private Integer steps;
    // Id生成加载器
    private GeneratorLoad generatorLoad;
    // 活跃线程服务
    private ExecutorService executorService;

    // 数据加载状态
    protected volatile boolean running;


    public AbstractGenerator(String bizName, Integer steps, GeneratorLoad generatorLoad, ExecutorService executorService) {
        this.bizName = bizName;
        this.steps = steps;
        this.generatorLoad = generatorLoad;
        this.executorService = executorService;
    }

    @Override
    public boolean loadOverflow() {
        return false;
    }
}
