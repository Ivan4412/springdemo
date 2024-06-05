package guavaIntern;

import com.google.common.collect.Interner;

/**
 * TestInterns
 *
 * @author yjs
 * @date 2024/3/13
 */
public class TestInterns implements Runnable {
    private String lock;
    private int out;
    private Interner<String> pool;

    public TestInterns(Interner<String> pool, String lock, int out) {
        this.lock = lock;
        this.out = out;
        this.pool = pool;
    }

    @Override
    public void run() {
        //重点
        synchronized (pool.intern(lock)) {
            System.out.println("--" + out);
            System.out.println(out + lock);
            System.out.println();
        }
    }

}
