package labmbda;

import java.util.Arrays;
import java.util.List;

/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2021/4/20
 * @description :
 */
public class LambdaDemo {
    public static void main(String[] args) {
        Arrays.asList("d", "b", "c").forEach((String e) -> System.out.print(e + ","));

        List<String> list = Arrays.asList("d", "b", "c");
        list.sort((e1, e2) -> {
            int result = e1.compareTo(e2);
            return result;
        });
        System.out.println("");
        list.forEach((String e) -> System.out.print(e + ","));


        list.sort((e1, e2) -> e1.compareTo(e2));
        System.out.println("");
        list.forEach((String e) -> System.out.print(e + ","));

        list.sort(String::compareTo);
        System.out.println("");
        list.forEach((String e) -> System.out.print(e + ","));
    }
}
