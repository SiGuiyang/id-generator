package quick.pager.id.generator.web.service;

import quick.pager.id.generator.resp.Response;

public interface IdService {

    /**
     * 根据bizName 获取id号段
     *
     * @param bizName bizName
     */
    Response<Long> getGeneratorId(String bizName);
}
