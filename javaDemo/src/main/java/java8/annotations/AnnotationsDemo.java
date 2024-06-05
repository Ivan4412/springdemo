package java8.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2021/4/21
 * @description : 注解的扩展
 * 新增加了两个注解的程序元素类型ElementType.TYPE_USE 和ElementType.TYPE_PARAMETER ，这两个新类型描述了可以使用注解的新场合。
 * 原文链接：https://blog.csdn.net/hj7jay/article/details/51249763
 */
public class AnnotationsDemo {
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
    public @interface NonEmpty {
    }

    public static class Holder<@NonEmpty T> extends @NonEmpty Object {
        public void method() throws @NonEmpty Exception {
        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        final Holder<String> holder = new @NonEmpty Holder<String>();
        @NonEmpty Collection<@NonEmpty String> strings = new ArrayList<>();
    }
}
