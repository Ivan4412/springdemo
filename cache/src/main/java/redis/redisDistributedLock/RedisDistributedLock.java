package redis.redisDistributedLock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2021/6/22
 * @description :
 */
public class RedisDistributedLock {
    private static final String LOCK_SUCCESS = "OK";
    private static JedisPool jedisPool = null;
    private static volatile RedisDistributedLock redisDistributedLock = null;
    //默认锁超时时间
    private static long defaultExpireMills = 15 * 1000;
    //默认和最小sleep时时间
    private static long defaultSleepMills = 500;
    //默认重试时时间
    private static long defaultRetryMills = 15 * 1000;

    private static final Long RELEASE_SUCCESS = 1L;

    private RedisDistributedLock() {
    }

    public static RedisDistributedLock getInstance() {
        if (jedisPool == null) {
            System.out.println("before getInstance must setJedisPool");
            return null;
        }
        if (redisDistributedLock == null) {
            synchronized (RedisDistributedLock.class) {
                if (redisDistributedLock == null) {
                    redisDistributedLock = new RedisDistributedLock();
                }
            }
        }
        return redisDistributedLock;
    }

    public static void setJedisPool(JedisPool jedisPool) {
        RedisDistributedLock.jedisPool = jedisPool;
    }

    public static void setDefaultExpireMills(long defaultExpireMills) {
        RedisDistributedLock.defaultExpireMills = defaultExpireMills;
    }

    public static void setDefaultSleepMills(long defaultSleepMills) {
        RedisDistributedLock.defaultSleepMills = defaultSleepMills;
    }

    public static void setDefaultRetryMills(long defaultRetryMills) {
        RedisDistributedLock.defaultRetryMills = defaultRetryMills;
    }

    public String getDistributedLock(String busiName, String busiKey) {
        return getDistributedLock(busiName, busiKey, 30 * 1000);
    }

    public String getDistributedLock(String busiName, String busiId, int expireMills) {
        if (busiName == null || busiId == null || busiName.length() == 0 || busiId.length() == 0) {
            throw new RedisDistributedLockException("invalid argument");
        }
        Jedis jedis = jedisPool.getResource();
        String result = null;
        try {
            result = getDistributedLock(jedis, busiName, busiId, UUID.randomUUID().toString(), expireMills);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        if (result == null) {
            throw new RedisDistributedLockException("获取分布式锁失败");
        }
        return result;
    }

    /**
     * 尝试获取分布式锁
     *
     * @param jedis
     * @param busiName
     * @param busiId
     * @param requestId
     * @param expireMills
     * @return
     */
    private String getDistributedLock(Jedis jedis, String busiName, String busiId, String requestId, long expireMills) {
        if (expireMills < 0) {
            expireMills = defaultExpireMills;
        }

        String result = jedis.set("LOCK-" + busiName + "-" + busiId, requestId, new SetParams().nx().px(expireMills));
        if (!LOCK_SUCCESS.equals(result)) {
            return null;
        }
        System.out.println(new Date().toString() + "|core get lock:" + busiName + " success,expireMills:" + expireMills);
        return requestId;
    }

    /**
     * 释放分布式锁
     *
     * @param busiName
     * @param busiId
     * @param requestId
     * @return
     */
    public boolean releaseDistributedLock(String busiName, String busiId, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedisPool.getResource().eval(script, Collections.singletonList("LOCK-" + busiName + "-" + busiId), Collections.singletonList(requestId));
        if (RELEASE_SUCCESS.equals(result)) {
            System.out.println(new Date().toString() + "|core release lock:" + "LOCK-" + busiName + "-" + busiId + " success");
            return true;
        }
        return false;
    }

    /**
     * 尝试获取分布式锁(默认30秒超时，重试10秒，间隔1秒)
     *
     * @param busiName
     * @param busiId
     * @return
     */
    public String autoRetryLock(String busiName, String busiId) {
        return autoRetryLock(busiName, busiId, defaultExpireMills);
    }

    public String autoRetryLock(String busiName, String busiId, long expireMills) {
        return autoRetryLock(busiName, busiId, expireMills, defaultSleepMills, defaultRetryMills);
    }

    public String autoRetryLock(String busiName, String busiId, long expireMills, long retryMills) {
        return autoRetryLock(busiName, busiId, expireMills, defaultSleepMills, retryMills);
    }

    public String autoRetryLock(String busiName, String busiId, long expireMills, long sleepMills, long retryMills) {
        Jedis jedis = jedisPool.getResource();
        String result = null;
        try {
            result = autoRetryLock(jedis, busiName, busiId, UUID.randomUUID().toString(), expireMills, sleepMills, retryMills);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        if (result == null) {
            throw new RedisDistributedLockException("获取分布式自动超时锁失败");
        }
        return result;
    }

    /**
     * 尝试获取分布式锁
     *
     * @param jedis       jedis
     * @param busiName    业务名
     * @param busiId      业务id
     * @param requestId   请求标识（用于释放锁）
     * @param expireMills 超期时间
     * @param sleepMills  重试间隔(秒)
     * @param retryMills  重试总时间(秒)
     * @return requestId      请求标识（用于释放锁）
     */
    private String autoRetryLock(Jedis jedis, String busiName, String busiId, String requestId, long expireMills, long sleepMills, long retryMills) {
        System.out.println(new Date().toString() + "|------开始获取自动锁，retry:" + retryMills + ",sleepMills:" + sleepMills + ",busiName:" + busiName + ",expireMills:" + expireMills);
        if (expireMills < 0) {
            expireMills = defaultExpireMills;
        }

        //sleep等待时间不能少于500ms
        if (sleepMills < defaultSleepMills) {
            sleepMills = defaultSleepMills;
        }

        long endTime = System.currentTimeMillis() + retryMills;

        String result = null;

        do {
            //尝试获取锁
            if (getDistributedLock(jedis, busiName, busiId, requestId, expireMills) != null) {
                //获取成功，直接返回
                System.out.println(new Date().toString() + "|------自动获取锁成功！busiName:" + busiName);
                result = requestId;
            } else {
                //获取失败，sleep 再重试
                System.out.println(new Date().toString() + "|获取锁失败，" + sleepMills + "ms后重试，retry:" + retryMills + ",busiName:" + busiName);
                try {
                    TimeUnit.MILLISECONDS.sleep(sleepMills);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                retryMills = retryMills - sleepMills;
            }
        } while (result == null && System.currentTimeMillis() <= endTime);

        if (result == null) {
            System.out.println(new Date().toString() + "|------获取锁超时，retry:" + retryMills + ",busiName:" + busiName);
            return null;
        }
        return result;
    }
}
