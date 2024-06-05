package java11.stringApi;

import java.util.stream.Stream;

/**
 * StringDemo
 *
 * @author yjs
 * @date 2024/6/5
 */
public class StringDemo {
    public static void main(String[] args) {
        //isBlank() ：检查字符串是否为空或仅包含空白字符（如空格、制表符、换行符等）。
        String str1 = "";
        String str2 = " ";
        String str3 = " \t ";
        System.out.println(str1.isBlank()); // 输出: true
        System.out.println(str2.isBlank()); // 输出: true
        System.out.println(str3.isBlank()); // 输出: true

        // lines() 方法 ：返回一个流（Stream），该流由字符串按行分隔而成。
        String str5 = "a\nb\nc";
        Stream<String> lines = str5.lines();
        lines.forEach(System.out::println); // 输出: a, b, c

        //repeat(int count)  ：返回一个新的字符串，该字符串是由原字符串重复指定次数形成的。
        String str6 = "abc";
        String repeatedStr = str6.repeat(3);
        System.out.println(repeatedStr); // 输出: abcabcabc

        //  strip()：删除字符串的前导和尾随空白字符。
        //  stripLeading()：删除字符串的前导空白字符。
        //  stripTrailing()：删除字符串的尾随空白字符。
        String str = " \t Hello, World! \n";
        System.out.println(str.strip()); // 输出: Hello, World!
        System.out.println(str.stripLeading()); // 输出: Hello, World! \n
        System.out.println(str.stripTrailing()); // 输出: \t Hello, World!


    }
}
