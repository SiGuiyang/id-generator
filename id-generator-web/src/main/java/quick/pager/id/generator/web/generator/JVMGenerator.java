package quick.pager.id.generator.web.generator;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import quick.pager.id.generator.web.model.Segment;
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

    public JVMGenerator(String bizName, Integer steps, GeneratorLoad generatorLoad, ExecutorService executorService) {
        super(bizName, steps, generatorLoad, executorService);
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
        return BLOCKING_DEQUE.isEmpty() || (this.getSteps() * 0.8 <= BLOCKING_DEQUE.size());
    }

    @Override
    public void load() {

        if (!this.isRunning()) {
            synchronized (this.getBizName()) {
                setRunning(true);
                getExecutorService().execute(() -> {
                    Segment segment = getGeneratorLoad().load();
                    long start = segment.getSegment() + 1;
                    Integer steps = segment.getSteps();

                    for (int i = 0; i < steps; i++) {
                        BLOCKING_DEQUE.offer(start + i);
                    }

                    setRunning(false);
                });
            }
        }
    }
}
