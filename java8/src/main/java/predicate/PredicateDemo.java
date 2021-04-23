package predicate;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2021/4/22
 * @description :
 * Predicate 是一个可以指定入参类型，并返回 boolean 值的函数式接口。它内部提供了一些带有默认实现的方法，可以 被用来组合一个复杂的逻辑判断（and, or, negate）
 */
public class PredicateDemo {
    public static void main(String[] args) {
        Predicate<String> predicate = (s) -> s.length() > 1;
        System.out.println(predicate.test("foo"));
        System.out.println(predicate.negate().test("foo"));


        Predicate<String> nonNull = Objects::nonNull;
        Predicate<String> isNull = Objects::isNull;
        System.out.println(nonNull.test(null));
        System.out.println(isNull.test(null));


        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> isNotEmpty = isEmpty.negate();
        System.out.println(isEmpty.test("foo"));
        System.out.println(isNotEmpty.test("foo"));

    }
}
