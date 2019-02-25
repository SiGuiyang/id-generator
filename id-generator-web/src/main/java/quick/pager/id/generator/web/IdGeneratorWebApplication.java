package quick.pager.id.generator.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import quick.pager.id.generator.web.redis.GeneratorRedisTemplate;

@SpringBootApplication
public class IdGeneratorWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdGeneratorWebApplication.class, args);
    }


    @Bean
    public GeneratorRedisTemplate generatorRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new GeneratorRedisTemplate(connectionFactory);
    }
}
