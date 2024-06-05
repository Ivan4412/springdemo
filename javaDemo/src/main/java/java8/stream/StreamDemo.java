package java8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



class Cat {
    int age;
    String name;

    public Cat(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2021/4/19
 * @description :
 * 1、流是什么：
    流是一种以声明性的方式来处理数据的API
    流是从支持数据处理操作的源生成的元素序列

 * 2、流的操作流程：
    创建流 -> 中间操作 -> 终端操作
    中间操作只是声明，不真实处理数据，直到终端操作开始才会执行

 * 3、循环合并：中间操作会自由组合（流根据系统自己来决定组合的顺序）

 * 4、短路技巧：如果中间操作处理的数据已经达到需求，则会立即停止处理数据（比如limit(1)，则当处理完1个就会停止处理）

 * 5、 流式操作和集合操作的区别：
    流按需处理，集合全处理
    流主攻数据处理，集合主攻数据存储
    流简洁，集合不
    流内部迭代（只迭代一次，完后流会消失），集合外部迭代（可一直迭代）
 *
 * 作者：汤圆学Java
 * 链接：https://juejin.cn/post/6952372605160521758
 */
public class StreamDemo {
    public static void main(String[] args) {
        List<Cat> list = Arrays.asList(new Cat(1, "tuantuan"), new Cat(2, "yuanyuan"), new Cat(3, "pingping"), new Cat(4, "anan"));
        List<String> listNameNew = list.stream().filter(cat -> {
            System.out.println("filter:" + cat);
            return cat.getAge() > 1;
        }).map(cat -> {
            System.out.println("map:" + cat);
            return cat.getName();
        })
        //只取第一个结果  .limit(1)
        .collect(Collectors.toList());

        System.out.println("result:" + listNameNew);
    }
}
