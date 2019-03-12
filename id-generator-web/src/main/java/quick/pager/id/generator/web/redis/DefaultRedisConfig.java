package quick.pager.id.generator.web.redis;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
@ConditionalOnProperty(prefix = "id.generator", name = "redis", havingValue = "true", matchIfMissing = false)
public class DefaultRedisConfig {

    @Bean
    public GeneratorRedisTemplate generatorRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new GeneratorRedisTemplate(connectionFactory);
    }
}