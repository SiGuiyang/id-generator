package quick.pager.id.generator.web;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.test.context.junit4.SpringRunner;
import quick.pager.id.generator.web.model.Segment;
import quick.pager.id.generator.web.redis.GeneratorRedisTemplate;


@RunWith(SpringRunner.class)
@SpringBootTest
public class IdGeneratorWebApplicationTests {

    @Autowired
    private GeneratorRedisTemplate generatorRedisTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_ONE_SQL = "select `id`, `biz_name`, `segment`,  `biz_type`, `steps`, `create_time`, `delete_status` from `id_generator` where `biz_name` = ? and `delete_status` = 0";

    private static final String UPDATE_LOAD_SEGMENT_SQL = "update `id_generator` set `segment`= `segment` + `steps` where `biz_name` = ?";


    @Test
    public void contextLoads() {
//
//        Long[] longs = new Long[10];
//        long m = 10L;
//        for (int i = 0; i < 10; i++) {
//            longs[i] = m + i;
//        }
//
//        generatorRedisTemplate.opsForList().leftPushAll("test.ids2", 1L, 2L, 3L, 4L, 5L, 6L, 7L, 7L, 8L, 9L);
//        generatorRedisTemplate.opsForList().leftPushAll("test.ids", longs);
//        for (int i = 0; i < 11; i++) {
//
//            System.out.println(generatorRedisTemplate.opsForList().rightPop("test.ids"));
//        }
//
//
//        Boolean flag = generatorRedisTemplate.execute(new RedisCallback<Boolean>() {
//            @Nullable
//            @Override
//            public Boolean doInRedis(@NonNull RedisConnection connection) throws DataAccessException {
//                try {
//                    return connection.setNX("demo3".getBytes("UTF-8"), "1".getBytes("UTF-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                return false;
//            }
//        });
//
//        System.out.println(flag);
//
//
//        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//        Long demo2 = generatorRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList("demo3"), 1);
//
//        System.out.println(demo2);


        int update = jdbcTemplate.update(UPDATE_LOAD_SEGMENT_SQL, "test");
        System.out.println(update);

    }

    @Test
    public void testJdbcTemplate() {
        Segment idGenerator = jdbcTemplate.queryForObject(SELECT_ONE_SQL, new Object[]{"test"}, new BeanPropertyRowMapper<>(Segment.class));
        System.out.println(idGenerator);


    }


    private static final String KEYS = "abc:1234";

    /**
     * 释放锁
     */
    private void unLock() {
        System.out.println("========================1111");
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object execute = generatorRedisTemplate.execute(new DefaultRedisScript<>(script,Long.class), Collections.singletonList(KEYS), 1);
        System.out.println(execute);
        System.out.println("========================3333");
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
                    return connection.setNX(KEYS.getBytes("UTF-8"), "1".getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    @Test
    public void testRedisKeys() {
        try {
            if (setRedisLock()) {
                System.out.println("lock");
            }
        } finally {
            unLock();
            System.out.println("unlock");

        }
    }

}
