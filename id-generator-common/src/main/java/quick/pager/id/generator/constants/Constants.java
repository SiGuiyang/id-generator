package quick.pager.id.generator.constants;

public interface Constants {

    /**
     * 获取Id生成器号段号段方式
     */
    enum BizType {

        JVM(0, "JVM"),
        REDIS(1, "redis");

        private int type;

        private String name;

        BizType(int type, String name) {
            this.type = type;
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }
}
