package quick.pager.id.generator.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import quick.pager.id.generator.resp.Response;
import quick.pager.id.generator.web.service.IdService;

/**
 * id 生成器的服务
 *
 * @author siguiyang
 */
@RestController
public class IdGeneratorController {


    @Autowired
    private IdService idService;

    /**
     * 对外暴露的id 生成器的服务
     *
     * @param bizName 某个张表生成的表示方案存储
     */
    @PostMapping("/id/generator/{bizName}")
    public Response<Long> idGenerator(@PathVariable("bizName") String bizName) {
        return idService.getGeneratorId(bizName);
    }
}
