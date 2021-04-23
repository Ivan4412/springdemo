package defaultMethod;


import java.util.function.Supplier;

/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2021/4/20
 * @description :
 * 默认方法允许我们在接口里添加新的方法，而不会破坏实现这个接口的已有类的兼容性，也就是说不会强迫实现接口的类实现默认方法。
 * <p>
 * 默认方法和抽象方法的区别是抽象方法必须要被实现，默认方法不是。
 * <p>
 * 原文链接：https://blog.csdn.net/hj7jay/article/details/51249763
 */

interface Defaultable {

    //Interfaces now allow default methods, the implementer may or  may not implement (override) them.

    default String notRequired() {
        return "Default implementation";
    }
}

class DefaultableImpl implements Defaultable {
}

class OverridableImpl implements Defaultable {
    @Override
    public String notRequired() {
        return "Overridden implementation";
    }
}


/**
 * Java 8 的另外一个有意思的新特性是接口里可以声明静态方法，并且可以实现
 */
interface DefaulableFactory {
    // Interfaces now allow static methods
    static Defaultable create(Supplier<Defaultable> supplier) {
        return supplier.get();
    }
}

public class DefaultMethodDemo {
    public static void main(String[] args) {
        Defaultable defaultable = DefaulableFactory.create(DefaultableImpl::new);
        System.out.println(defaultable.notRequired());

        defaultable = DefaulableFactory.create(OverridableImpl::new);
        System.out.println(defaultable.notRequired());

    }
}
