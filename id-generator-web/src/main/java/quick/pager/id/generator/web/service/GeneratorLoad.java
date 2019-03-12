package quick.pager.id.generator.web.service;

import quick.pager.id.generator.web.model.Segment;

@FunctionalInterface
public interface GeneratorLoad {

    /**
     * 加载一条IdGenerator 记录
     */
    Segment load();
}
