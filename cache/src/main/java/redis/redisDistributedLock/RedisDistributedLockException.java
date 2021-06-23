package redis.redisDistributedLock;

public class RedisDistributedLockException extends RuntimeException {
    public RedisDistributedLockException() {
        super("获取分布式锁异常");
    }

    public RedisDistributedLockException(String message) {
        super(message);
    }
}
