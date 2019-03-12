package quick.pager.id.generator.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import quick.pager.id.generator.MybatisIdGeneratorInterceptor;

@SpringBootApplication
@MapperScan("quick.pager.id.generator.demo.mapper")
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.setProperty("id.generator.requestUrl","http://localhost:8080");
    }

    @Bean
    public MybatisIdGeneratorInterceptor mybatisIdGeneratorInterceptor() {
        return new MybatisIdGeneratorInterceptor();
    }

}
