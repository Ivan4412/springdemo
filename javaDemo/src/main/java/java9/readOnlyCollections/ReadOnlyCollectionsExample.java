package java9.readOnlyCollections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * ReadOnlyCollectionsExample
 *
 * @author yjs
 * @date 2024/6/3
 */
public class ReadOnlyCollectionsExample {
    public static void main(String[] args) {
        // 创建一个只读的List
        List<String> readOnlyList = List.of("a", "b", "c");
        // 尝试向只读列表添加元素将抛出UnsupportedOperationException异常
        // readOnlyList.add("d");

        // 创建一个只读的Set
        Set<String> readOnlySet = Set.of("x", "y", "z");
        // 尝试向只读集合添加元素将抛出UnsupportedOperationException异常
        // readOnlySet.add("w");

        // 使用工厂方法创建一个只读的List
        List<String> anotherReadOnlyList = Collections.unmodifiableList(new ArrayList<>(List.of("foo", "bar", "baz")));
        // 现在尝试添加元素不会抛出异常，因为我们使用了工厂方法创建了一个可修改的包装列表
        // anotherReadOnlyList.add("qux");
    }

}
