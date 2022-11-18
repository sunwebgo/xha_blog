package com.xha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.xha.mapper")
@SpringBootApplication
public class SanGengAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(SanGengAdminApplication.class,args);
    }
}
