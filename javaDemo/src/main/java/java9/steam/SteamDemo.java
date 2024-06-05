package java9.steam;

import java.util.stream.Stream;

/**
 * SteamDemo
 * takeWhile 和 dropWhile：这两个方法允许开发者根据给定的条件从流中获取或丢弃满足条件的元素，直到遇到第一个不满足条件的元素。
 * takeWhile：处理流中所有的数据，直到条件 predicate 返回 false 为止。
 * dropWhile：与 takeWhile 作用相反，直到断言语句第一次返回 true 才返回给定 Stream 的子集。
 * @author yjs
 * @date 2024/6/3
 */
public class SteamDemo {
    public static void main(String[] args){
        Stream.of(1, 2, 3, 4, 5, 1, 7, 8, 9)
                .takeWhile(n -> n < 5)
                .forEach(System.out::println); // 输出 1, 2, 3, 4

        Stream.of("a", "b", "c", "", "e", "f")
                .dropWhile(s -> !s.isEmpty())
                .forEach(System.out::print); // 输出 ef
    }
}
