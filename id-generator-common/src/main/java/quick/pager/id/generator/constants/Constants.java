package quick.pager.id.generator.constants;

public interface Constants {

    /**
     * 获取Id生成器号段号段方式
     */
    enum BizType {

        JVM(0, "JVM"),
        REDIS(1, "redis"),
        ZOOKEEPER(2,"zookeeper");

        public int type;

        public String name;

        BizType(int type, String name) {
            this.type = type;
            this.name = name;
        }

    }
}
