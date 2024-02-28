package org.lin.dynamicdatasource.properties;

public class DbRouterMarkContext {

    private static final ThreadLocal<String> dbRouterMarkThreadLocal = new ThreadLocal<>();

    public static void set(String dbRouterMark) {
        dbRouterMarkThreadLocal.set(dbRouterMark);
    }

    public static String get() {
        return dbRouterMarkThreadLocal.get();
    }

    public static void clear() {
        dbRouterMarkThreadLocal.remove();
    }
}
