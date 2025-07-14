package finku.ukim.mk.eduai.config;

public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void useRead() {
        contextHolder.set("READ");
    }

    public static void useWrite() {
        contextHolder.set("WRITE");
    }

    public static String getDataSourceType() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}
