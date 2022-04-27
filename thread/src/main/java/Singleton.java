/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2022/4/1
 * @description :
 */
public class Singleton {
    private volatile static Singleton uniqueInstance;

    private Singleton() {

    }

    public Singleton getUniqueInstance() {
        //先判断对象是否已经实例过，没有实例化过才进入加锁代码
        if (uniqueInstance == null) {
            //类对象加锁
            synchronized (Singleton.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Singleton();
                }
            }
        }
        return uniqueInstance;
    }
}
