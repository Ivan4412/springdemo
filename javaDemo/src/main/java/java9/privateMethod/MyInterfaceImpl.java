package java9.privateMethod;

/**
 * MyInterfaceImpl
 *
 * @author yjs
 * @date 2024/6/3
 */
public class MyInterfaceImpl implements MyInterface {
    // 实现接口的默认方法，将会自动继承接口的私有方法


    public static void main(String[] args) {
        MyInterfaceImpl impl = new MyInterfaceImpl();
        impl.printMessage(); // 输出：This is a private message
        MyInterface.doStaticWork(); // 输出：This is a private static message
    }
}
