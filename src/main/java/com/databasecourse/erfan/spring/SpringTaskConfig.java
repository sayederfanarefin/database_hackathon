package com.databasecourse.erfan.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan({ "com.databasecourse.erfan.task" })
public class SpringTaskConfig {

}
