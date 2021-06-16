package guavaCache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2021/5/26
 * @description :
 */
public class GuavaCacheDemo {
    public static void main(String[] args) {

        Cache<String, String> myMap = CacheBuilder.newBuilder()
                //根据某个键值对最后一次访问之后多少时间后移除
                .expireAfterAccess(30L, TimeUnit.SECONDS)
                //根据某个键值对被创建或值被替换后多少时间移除
                .expireAfterWrite(3L, TimeUnit.MINUTES)
                //指定允许同时更新的操作数,若不设置CacheBuilder默认为4,这个参数会影响缓存存储空间的分块,可以简单理解为,默认会创建指定size个map,
                // 每个map称为一个区块,数据会分别存到每个map里
                .concurrencyLevel(4)
                //指定缓存初始化的空间大小,如果设置了40,并且concurrencyLevel取默认,会分成4个区块,每个区块最大的size为10
                // ,当更新数据时,会对这个区块进行加锁,这就是为什么说,允许同时更新的操作数为4,
                // 延伸一点,在淘汰数据时,也是每个区块单独维护自己的淘汰策略.也就是说,如果每个区块size太大,竞争就会很激烈.
                .initialCapacity(40)
                //最大缓存条数，即将到达指定的大小，那就会把不常用的键值对从cache中移除。
                .maximumSize(1000)
                //软引用模式，按照LRU的原则来定期GC回收
                .softValues()
                .build();

        myMap.put("name", "张三");

        System.out.println(myMap.getIfPresent("name"));


        Cache<String, Object> cache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).maximumSize(500).build();

       //1.  CacheLoader方式
       // 在构建Cache对象的时候定义一个CacheLoader来获取数据，在缓存不存在的时候能够自动加载数据到缓存中。
       // 这种创建方式使用的场景是：在创建的时候采用制定的加载缓存的方式，经常用作从数据库中获取和缓存数据。
        LoadingCache<String, Object> loadingCache = CacheBuilder.newBuilder()
                .expireAfterAccess(30, TimeUnit.SECONDS).maximumSize(5)
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(String key) throws Exception {
                        return "value1";
                    }
                });
        try {
            System.out.println(loadingCache.getIfPresent("key"));
            System.out.println(loadingCache.get("key"));
            System.out.println(loadingCache.get("key"));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        // 2. Callable callback 方式
       //  这个方法返回缓存中响应的值，如果未获取到缓存则调用Callable方法。这个方法简便地实现了“如果有缓存则返回，否则读取、缓存、返回”的模式。
       //  这种方法比较灵活，可以在获取不到对象的时候，动态决定采用哪种方式加载数据到缓存。
        try {
            cache.get("key", new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return "value2";
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println(cache.getIfPresent("key"));
    }
}
