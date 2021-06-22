package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2021/6/18
 * @description :
 */
public class RedisLock {
    //key
    private static String LOCK_KEY = "redis_lock";

    //自动失效时间
    private static Long INTERNAL_LOCK_LEASE_TIME = 3L;

    //超时时间还没拿到，自动退出
    private static Long TIMEOUT = 1000L;

    private SetParams params = SetParams.setParams().nx().px(INTERNAL_LOCK_LEASE_TIME);

    private static Jedis jedis = new JedisPool("127.0.0.1", 6379).getResource();

    /**
     * 加锁
     *
     * @param id
     * @return
     */
    public boolean lock(String id) {
        Long start = System.currentTimeMillis();
        try {
            for (; ; ) {
                //SET命令返回OK，则证明获取锁成功
                String lock = jedis.set(LOCK_KEY, id, params);
                if ("OK".equals(lock)) {
                    return true;
                }
                //否则循环等待，在timeout时间内扔未获取到锁，则获取锁失败
                long l = System.currentTimeMillis() - start;
                if (l >= TIMEOUT) {
                    return false;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            jedis.close();
        }
    }

    /**
     * 解锁 * * @param id * @return
     */
    public boolean unlock(String id) {
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then"
                + "   return redis.call('del',KEYS[1]) "
                + "else"
                + "   return 0 "
                + "end";
        try {
            String result = jedis.eval(script, Collections.singletonList(LOCK_KEY), Collections.singletonList(id)).toString();
            return "1".equals(result) ? true : false;
        } finally {
            jedis.close();
        }
    }


}
