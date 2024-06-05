package java8.repeatable;

import java.lang.annotation.*;

/**
 * MyAnnotation
 *
 * @Repeatable注解是用来标注一个注解在同一个地方可重复使用的一个注解，
 * 比如说你定义了一个注解，如果你的注解没有标记@Repeatable这个JDK自带的注解，那么你这个注解在引用的地方就只能使用一次。
 *
 * @author yjs
 * @date 2024/5/28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = repeatable.MyAnnotationList.class)
public @interface MyAnnotation {
    String name();
}
