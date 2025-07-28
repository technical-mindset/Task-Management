package com.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories(basePackages = {"com.taskdb.repository"} )
@EntityScan(basePackages = "com.taskdb.model")
@SpringBootApplication
@EnableScheduling
public class TaskManagementAPI extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagementAPI.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TaskManagementAPI.class);
    }

}
