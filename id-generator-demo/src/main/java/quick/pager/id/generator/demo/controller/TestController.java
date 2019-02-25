package quick.pager.id.generator.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quick.pager.id.generator.demo.service.JPATestService;
import quick.pager.id.generator.demo.service.MybatisTestService;

/**
 * id 生成器的测试
 *
 * @author siguiyang
 */
@RestController
@RequestMapping("/test")
public class TestController {


    @Autowired
    private MybatisTestService mybatisTestService;

    @Autowired
    private JPATestService jpaTestService;

    @GetMapping("/mybatis")
    public String testMybatis() {
        mybatisTestService.insert();
        return "success";
    }

    @GetMapping("/selectMybatis")
    public String testSelectMybatis() {
        mybatisTestService.testSelectMybatis();
        return "success";
    }

    public String testJPA() {
        jpaTestService.insert();
        return "success";
    }

}
