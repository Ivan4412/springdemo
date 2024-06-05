package java8.repeatingAnnotation;

import java.lang.annotation.*;

/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2021/4/20
 * @description :
 *              java8 引入了重复注释，允许相同注释在声明使用的时候重复使用超过一次
 *              参考：https://docs.oracle.com/javase/tutorial/java/annotations/repeating.html
 */

public class RepeatingAnnotations {
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Filters {
        Filter[] value();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(Filters.class)
    public @interface Filter {
        String value();
    }

    @Filter("filter1")
    @Filter("filter2")
    public interface Filterable {
    }

    public static void main(String[] args) {
        for (Filter filter : Filterable.class.getAnnotationsByType(Filter.class)) {
            System.out.println(filter.value());
        }
    }

}