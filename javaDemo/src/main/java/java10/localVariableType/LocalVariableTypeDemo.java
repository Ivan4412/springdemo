package java10.localVariableType;

import java.util.List;

/**
 * LocalVariableTypeDemo
 * 局部变量的类型推断
 * @author yjs
 * @date 2024/6/5
 */
public class LocalVariableTypeDemo {
    public static void main(String[] args) {
        // 使用类型推断来声明变量
        var number = 10; // 编译器自动推断为 int 类型
        var text = "Hello, World!"; // 自动推断为 String 类型
        var list = List.of(1, 2, 3, 4, 5); // 自动推断为 List<Integer> 类型

        // 输出变量的值
        System.out.println(number);
        System.out.println(text);
        System.out.println(list);
    }
}
