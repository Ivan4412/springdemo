import java.util.UUID;

/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2021/6/16
 * @description :
 */
public class UUIDTest {
    public static void  main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(UUID.randomUUID().toString().replace("-", ""));
        }
    }
}
