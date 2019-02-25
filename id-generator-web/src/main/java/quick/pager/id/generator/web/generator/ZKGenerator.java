package quick.pager.id.generator.web.generator;

import java.util.concurrent.ExecutorService;
import quick.pager.id.generator.web.service.GeneratorLoad;

/**
 * 基于zookeeper 方式生成ID策略
 *
 * @author siguiyang
 */
public class ZKGenerator extends AbstractGenerator {


    public ZKGenerator(String bizName, GeneratorLoad generatorLoad, ExecutorService executorService) {
        super(bizName, generatorLoad, executorService);
    }

    @Override
    public Number getGeneratorId() {
        return null;
    }

    @Override
    public void load() {

    }
}
