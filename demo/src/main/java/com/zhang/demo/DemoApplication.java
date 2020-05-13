package com.zhang.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true) // 用于测试方法间调用事务方法失效问题（通过代理调用事务方法）
//@EnableScheduling // 启用定时任务
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("启动成功");
    }

}
