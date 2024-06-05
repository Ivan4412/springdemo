package java8.optional;

import java.util.Optional;

/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2021/4/21
 * @description :
 */
public class OptionalDemo {
    public static void main(String[] args) {
        Optional<String> fullName = Optional.ofNullable(null);
        System.out.println("Full Name is set? " + fullName.isPresent());
        System.out.println("Full Name: " + (fullName = Optional.of(fullName.orElseGet(() -> "None"))));
        System.out.println(fullName.map(s -> "Hey " + s + "!").orElse("Hey Stranger!"));

    }
}
