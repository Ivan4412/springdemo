package redis;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2021/6/18
 * @description :
 */
public class RedissonLockDemo {

    private static Integer inventory = 1000;
    private static final int NUM = 1000;
    private static LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();

    public static void main(String[] args) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(inventory, inventory, 10L, TimeUnit.SECONDS, linkedBlockingQueue);
        long start = System.currentTimeMillis();

        final CountDownLatch countDownLatch = new CountDownLatch(NUM);

        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        final RedissonClient client = Redisson.create(config);

        final RLock lock = client.getLock("lock1");
        for (int i = 0; i < NUM; i++) {
            threadPoolExecutor.execute(() -> {
                lock.lock();
                inventory--;
                System.out.println(inventory);
                lock.unlock();
                countDownLatch.countDown();
            });
        }

        threadPoolExecutor.shutdown();
        try{
            countDownLatch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("执行线程数:" + NUM + "   总耗时:" + (end - start) + "  库存数为:" + inventory);

    }
}
