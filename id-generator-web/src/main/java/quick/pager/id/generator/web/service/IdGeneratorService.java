package quick.pager.id.generator.web.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import quick.pager.id.generator.constants.Constants;
import quick.pager.id.generator.resp.Response;
import quick.pager.id.generator.web.generator.IGenerator;
import quick.pager.id.generator.web.generator.JVMGenerator;
import quick.pager.id.generator.web.generator.RedisGenerator;
import quick.pager.id.generator.web.model.IdGenerator;
import quick.pager.id.generator.web.redis.GeneratorRedisTemplate;

@Service
@Slf4j
public class IdGeneratorService implements IdService, InitializingBean {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GeneratorRedisTemplate generatorRedisTemplate;


    /**
     * Id 生成器缓存
     */
    private static final Map<String, IGenerator> CACHE = new ConcurrentHashMap<>(1000);

    // 查询所有的自增策略sql语句
    private static final String ALL_SQL = "select `id`, `biz_name`, `description`, `segment`,  `biz_type`, `steps`, `create_time`, `delete_status` from `id_generator` where `delete_status` = 0";
    // 根据biz_name 查询Id的自增策略
    private static final String SELECT_ONE_SQL = "select `id`, `biz_name`, `description`, `segment`,  `biz_type`, `steps`, `create_time`, `delete_status` from `id_generator` where `biz_name` = ? and `delete_status` = 0";
    // 根据biz_name 更新segment 值
    private static final String UPDATE_LOAD_SEGMENT_SQL = "update `id_generator` set `segment`= `segment` + `steps` where `biz_name` = ?";

    @Override
    public Response<Long> getGeneratorId(String bizName) {
        if (!CACHE.containsKey(bizName)) {
            return new Response<>(201, "不存在此biz_name = " + bizName + " 号段");
        }
        // 生成策略
        IGenerator iGenerator = CACHE.get(bizName);
        long value = iGenerator.getGeneratorId().longValue();
        System.out.println(value);
        return new Response<>(value);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("系统初始化加载数据开始。。。。");
        List<IdGenerator> generators = jdbcTemplate.query(ALL_SQL, new BeanPropertyRowMapper<>(IdGenerator.class));
        IGenerator iGenerator = null;
        for (IdGenerator generator : generators) {
            String bizName = generator.getBizName();
            // 从JVM 中获取号段
            if (Constants.BizType.JVM.type == generator.getBizType()) {

                iGenerator = new JVMGenerator(bizName, () -> loadIdGenerator(bizName)
                        , Executors.newWorkStealingPool());
            }
            // 从redis 中获取号段
            else if (Constants.BizType.REDIS.type == generator.getBizType()) {
                iGenerator = new RedisGenerator(bizName, () -> loadIdGenerator(bizName)
                        , Executors.newWorkStealingPool(), generatorRedisTemplate);
            }
            // 从zookeeper 中获取号段
            else if (Constants.BizType.ZOOKEEPER.type == generator.getBizType()) {

            } else {
                log.warn("未知Id生成策略 biz_name = {}" + bizName);
            }

            CACHE.putIfAbsent(bizName, iGenerator);
        }

        log.info("系统刚初始化加载完成。。。。");
    }


    private IdGenerator loadIdGenerator(String bizName) {

        int update = jdbcTemplate.update(UPDATE_LOAD_SEGMENT_SQL, bizName);
        if (update < 0) {
            throw new RuntimeException("号段值更新异常 bizName = " + bizName);
        }

        return jdbcTemplate.queryForObject(SELECT_ONE_SQL, new Object[]{bizName}, new BeanPropertyRowMapper<>(IdGenerator.class));
    }
}
