package quick.pager.id.generator.web.generator;

@FunctionalInterface
public interface IGenerator {

    /**
     * 获取当前的Id值
     */
    Number getGeneratorId();

    /**
     * 是否过载<br />
     * 过载说明需要重新加载一批数据进入缓存中
     *
     * @return true 过载 false 反之亦然
     */
    default boolean loadOverflow() {
        return false;
    }

    /**
     * 加载号段进入缓存
     */
    default void load() {
    }
}
