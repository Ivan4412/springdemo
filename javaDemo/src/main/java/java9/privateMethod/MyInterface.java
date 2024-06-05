package java9.privateMethod;

/**
 * MyInterface
 *
 * @author yjs
 * @date 2024/6/3
 */
public interface MyInterface {
    // 默认方法
    default void printMessage() {
        printPrivateMessage(); // 调用私有方法
    }

    // 静态方法
    static void doStaticWork() {
        printPrivateStaticMessage(); // 调用私有静态方法
    }

    // 私有方法
    private void printPrivateMessage() {
        System.out.println("This is a private message");
    }

    // 私有静态方法
    private static void printPrivateStaticMessage() {
        System.out.println("This is a private static message");
    }
}
