package quick.pager.id.generator.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import quick.pager.id.generator.IdGeneratorInterceptor;

@SpringBootApplication
@MapperScan("quick.pager.id.generator.demo.mapper")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public IdGeneratorInterceptor idGeneratorInterceptor() {
        IdGeneratorInterceptor idGeneratorInterceptor = new IdGeneratorInterceptor();
        idGeneratorInterceptor.setRequestUrl("http://localhost:8080");
        return idGeneratorInterceptor;
    }

}
