package quick.pager.id.generator.web.generator;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import quick.pager.id.generator.web.model.Segment;
import quick.pager.id.generator.web.redis.GeneratorRedisTemplate;
import quick.pager.id.generator.web.service.GeneratorLoad;

/**
 * redis Id生成器
 *
 * @author siguiyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RedisGenerator extends AbstractGenerator {

    // redis 操作模版
    private GeneratorRedisTemplate generatorRedisTemplate;
    // redis key
    private static final String ID_GENERATOR_KEYS = "id_generator_keys:";
    // redis key 锁
    private static final String ID_GENERATOR_LOCK_KEYS = "id_generator_lock_keys:";

    public RedisGenerator(String bizName, Integer steps, GeneratorLoad generatorLoad, ExecutorService executorService, GeneratorRedisTemplate generatorRedisTemplate) {
        super(bizName, steps, generatorLoad, executorService);
        this.generatorRedisTemplate = generatorRedisTemplate;
    }

    @Override
    public Number getGeneratorId() {
        if (loadOverflow()) {
            this.load();
        }

        return generatorRedisTemplate.opsForList().rightPop(getRedisKeys(), 300, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean loadOverflow() {
        Long size = generatorRedisTemplate.opsForList().size(getRedisKeys());

        return (null == size) || (0 == size) || (this.getSteps() * 0.9 <= size);
    }

    @Override
    public void load() {
        try {
            // 加锁
            if (setRedisLock()) {
                getExecutorService().execute(() -> {
                    Segment segment = getGeneratorLoad().load();
                    Long start = segment.getSegment() + 1;
                    Integer steps = segment.getSteps();
                    Long[] longs = new Long[steps];

                    for (int i = 0; i < steps; i++) {
                        longs[i] = start + i;
                    }

                    // 放入redis队列中
                    generatorRedisTemplate.opsForList().leftPushAll(getRedisKeys(), longs);
                });
            }
        } finally {
            // 释放锁
            unLock();
        }

    }


    /**
     * 释放锁
     */
    private void unLock() {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        generatorRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList(getRedisLockKeys()), 1);
    }

    /**
     * biz_name ID生成进入redis 的key
     */
    private String getRedisKeys() {
        return ID_GENERATOR_KEYS + getBizName();
    }

    /**
     * biz_name redis 锁 key
     */
    private String getRedisLockKeys() {
        return ID_GENERATOR_LOCK_KEYS + getBizName();
    }

    /**
     * 加锁
     */
    private Boolean setRedisLock() {
        return generatorRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Nullable
            @Override
            public Boolean doInRedis(@NonNull RedisConnection connection) throws DataAccessException {
                try {
                    return connection.setNX(getRedisLockKeys().getBytes("UTF-8"), "1".getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }
}
