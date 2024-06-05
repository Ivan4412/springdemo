package guavaIntern;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

/**
 * GuavaInternDemo
 *
 * @author yjs
 * @date 2024/3/13
 */
public class GuavaInternDemo {
    public static void main(String[] args) {
        Interner<String> pool = Interners.newWeakInterner();
        for (int i = 0; i < 10; i++) {
            String lock=new String("lock");
            TestInterns testInterns = new TestInterns(pool,lock, i);
            Thread thread = new Thread(testInterns);
            thread.start();
        }

    }
}
