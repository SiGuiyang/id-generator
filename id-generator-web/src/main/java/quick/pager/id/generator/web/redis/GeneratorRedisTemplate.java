package quick.pager.id.generator.web.redis;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.NonNull;

public class GeneratorRedisTemplate extends RedisTemplate<String,Long> {

    /**
     * Constructs a new <code>StringRedisTemplate</code> instance. {@link #setConnectionFactory(RedisConnectionFactory)}
     * and {@link #afterPropertiesSet()} still need to be called.
     */
    public GeneratorRedisTemplate() {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        RedisSerializer genericFastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        setKeySerializer(stringSerializer);
        setValueSerializer(genericFastJsonRedisSerializer);
        setHashKeySerializer(genericFastJsonRedisSerializer);
        setHashValueSerializer(genericFastJsonRedisSerializer);
    }

    /**
     * Constructs a new <code>StringRedisTemplate</code> instance ready to be used.
     *
     * @param connectionFactory connection factory for creating new connections
     */
    public GeneratorRedisTemplate(RedisConnectionFactory connectionFactory) {
        this();
        setConnectionFactory(connectionFactory);
        afterPropertiesSet();
    }

    @NonNull
    @Override
    protected RedisConnection preProcessConnection(@NonNull RedisConnection connection, boolean existingConnection) {
        return new DefaultStringRedisConnection(connection);
    }
}
