package quick.pager.id.generator.web.generator;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import quick.pager.id.generator.web.model.IdGenerator;
import quick.pager.id.generator.web.service.GeneratorLoad;

/**
 * 基于JVM 缓存生成器
 *
 * @author siguiyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JVMGenerator extends AbstractGenerator {

    // id 队列缓存
    private static final BlockingDeque<Long> BLOCKING_DEQUE = new LinkedBlockingDeque<>();

    public JVMGenerator(String bizName, GeneratorLoad generatorLoad, ExecutorService executorService) {
        super(bizName, generatorLoad, executorService);
    }

    @Override
    public Number getGeneratorId() {
        if (this.loadOverflow()) {
            this.load();
        }
        try {
            return BLOCKING_DEQUE.poll(300, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean loadOverflow() {
        return BLOCKING_DEQUE.isEmpty();
    }

    @Override
    public void load() {

        if (!this.isRunning()) {
            synchronized (this.getBizName()) {
                setRunning(true);
                getExecutorService().execute(() -> {
                    IdGenerator idGenerator = getGeneratorLoad().load();
                    Long start = idGenerator.getSegment() + 1;
                    Integer steps = idGenerator.getSteps();

                    for (int i = 0; i < steps; i++) {
                        BLOCKING_DEQUE.offer(start + i);
                    }

                    setRunning(false);
                });
            }
        }
    }
}
