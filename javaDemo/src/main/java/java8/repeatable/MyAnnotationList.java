package repeatable;

import java8.repeatable.MyAnnotation;

import java.lang.annotation.*;

/**
 * MyAnnotationList
 * 1、在引用注解上的@MyAnnotationList的方法中得有value()方法
 * 2、在引用注解上的@MyAnnotationList的方法中得有【被@Repeatable注解的@MyAnnotation注解类型的数组返回值的value方法】
 * 3、该案例中，若在方法上重复使用@MyAnnotation注解，实际上也会在运行的时候被包装成MyAnnotationList[] 里面
 * 4、@MyAnnotation可多次使用，但不可多次与@MyAnnotationList一起使用
 * @date 2024/5/28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotationList {
    MyAnnotation[] value();
}
