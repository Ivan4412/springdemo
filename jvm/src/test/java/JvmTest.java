/**
 * author : yjs
 * createTime : 2021/3/3
 * description :
 * version : 1.0
 */
public class JvmTest {

    public static void main(String[] args) {
        {
            byte[] placeholder = new byte[64 * 1024 * 1024];
        }
        int a = 0;
        System.gc();

    }
}
