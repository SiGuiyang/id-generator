package quick.pager.id.generator.web.service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import quick.pager.id.generator.constants.Constants;
import quick.pager.id.generator.resp.Response;
import quick.pager.id.generator.web.GeneratorContext;
import quick.pager.id.generator.web.dao.GeneratorDao;
import quick.pager.id.generator.web.generator.IGenerator;
import quick.pager.id.generator.web.generator.JVMGenerator;
import quick.pager.id.generator.web.generator.RedisGenerator;
import quick.pager.id.generator.web.generator.ZeroGenerator;
import quick.pager.id.generator.web.model.Segment;
import quick.pager.id.generator.web.redis.GeneratorRedisTemplate;

@Service
@Slf4j
public class IdGeneratorService implements IdService, InitializingBean {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static Random ran = new Random();

    private static ExecutorService service = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Integer.MAX_VALUE,
            60L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), r -> new Thread(r, "Thread-ID-Generator-" + ran.nextInt(10)));

    private GeneratorDao dao;

    /**
     * Id 生成器缓存
     */
    private static final Map<String, IGenerator> CACHE = new ConcurrentHashMap<>(32);


    @Override
    public Response<Long> getGeneratorId(String bizName) {
        if (!CACHE.containsKey(bizName)) {
            log.error("未找到对应的Id生成业务号段 bizName = {}", bizName);
            return new Response<>(201, "未找到对应的Id生成业务号段 = " + bizName + " 号段");
        }
        // 生成策略
        IGenerator iGenerator = CACHE.get(bizName);

        if (null == iGenerator) {
            log.error("iGenerator 未找到对应的Id生成业务号段 bizName = {}", bizName);
            iGenerator = new ZeroGenerator();
        }

        long value = iGenerator.getGeneratorId().longValue();

        return new Response<>(value);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("系统初始化加载数据开始。。。。");
        dao = new GeneratorDao(jdbcTemplate);
        List<Segment> segments = dao.selectAll();
        IGenerator iGenerator = null;
        for (Segment segment : segments) {
            String bizName = segment.getBizName();
            // 从JVM 中获取号段
            if (Constants.BizType.JVM.getType() == segment.getBizType()) {

                iGenerator = new JVMGenerator(bizName, segment.getSteps(), () -> dao.updateAndLoadSegment(bizName)
                        , service);
            }
            // 从redis 中获取号段
            else if (Constants.BizType.REDIS.getType() == segment.getBizType()) {

                try {

                    GeneratorRedisTemplate generatorRedisTemplate = GeneratorContext.getBean(GeneratorRedisTemplate.class);

                    iGenerator = new RedisGenerator(bizName, segment.getSteps(), () -> dao.updateAndLoadSegment(bizName)
                            , service, generatorRedisTemplate);
                } catch (Exception e) {
                    log.error("此业务号段是redis 方式，请配置【id.generator.redis=true】，业务号段 biz_name = {}", bizName);
                    iGenerator = new ZeroGenerator();
                }
            } else {
                log.warn("未知Id生成策略 biz_name = {}" + bizName);
            }

            CACHE.putIfAbsent(bizName, iGenerator);
        }

        log.info("系统刚初始化加载完成。。。。");
    }

}
