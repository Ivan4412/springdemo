package com.yjs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yjs.domain.mapper")
public class SpringbootMybaitsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMybaitsApplication.class, args);
	}
}