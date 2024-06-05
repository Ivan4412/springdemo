package java11.localVariableType;

import java.util.Arrays;
import java.util.function.BinaryOperator;

/**
 * LocalVariableTypeDemo
 * Java 11 中的局部变量类型推断增强:
 * 支持注解,可以在lambda表达式的形参中使用var，好处是可以在形参上加注解
 * Lambda 表达式中的使用
 *
 * @author yjs
 * @date 2024/6/5
 */
public class LocalVariableTypeDemo {
    public static void main(String[] args) {
        // Java 11 示例
        @Deprecated
        var deprecatedVar = "This is a deprecated variable";

        // Lambda 表达式中使用 var 并添加注解
        BinaryOperator<Integer> bo = (@Deprecated var value1, @Deprecated var value2) -> value1 + value2;

        System.out.println(bo);

        // Java 11 示例
        var list = Arrays.asList("a", "b", "c");
        list.forEach(item -> System.out.println(item));

    }
}
