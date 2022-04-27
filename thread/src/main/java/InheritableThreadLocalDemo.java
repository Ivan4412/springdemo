
/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2022/3/31
 * @description :
 *       可以看到当前内是可以访问到 ThreadLocal 和 InheritableThreadLocal 的数据的
 *       子线程只能访问 InheritableThreadLocal 的数据，不能访问 ThreadLocal 的数据
 */
public class InheritableThreadLocalDemo {
    private  static InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

        public static void main(String[] args) {
            // 使用InheritableThreadLocal 进行设置
            inheritableThreadLocal.set("123");
            new Thread((()->{
                System.out.println(Thread.currentThread().getName()+"--"+inheritableThreadLocal.get());
            })).start();
            System.out.println(Thread.currentThread().getName()+"--"+inheritableThreadLocal.get());



            // 使用ThreadLocal 进行设置
            threadLocal.set("hello world");
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName()+"-" + threadLocal.get());
            }).start();
            System.out.println(Thread.currentThread().getName()+"-"+threadLocal.get());

    }
}
