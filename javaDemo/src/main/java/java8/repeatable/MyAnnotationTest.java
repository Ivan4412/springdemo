package java8.repeatable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * MyAnnotationTest
 * <p>
 * 若在方法上重复使用@MyAnnotation注解，实际上也会在运行的时候被包装成MyAnnotationList[] 里面
 *
 * @author yjs
 * @date 2024/5/28
 */
public class MyAnnotationTest {

    @MyAnnotation(name = "test1")
    @MyAnnotation(name = "test2")
  //  @MyAnnotationList(value = { @MyAnnotation(name = "test1"), @MyAnnotation(name = "test2")})
    private void testMethod() {

    }

    public static void main(String[] args) {
        final Method[] declaredMethods = MyAnnotationTest.class.getDeclaredMethods();
        for (Method method : declaredMethods) {
            System.out.println(method.getName() + "上的注释有：");
            final Annotation[] declaredAnnoations = method.getDeclaredAnnotations();
            for (Annotation annotation : declaredAnnoations) {
                System.out.println(annotation);
            }
            System.out.println();
        }
    }
}
