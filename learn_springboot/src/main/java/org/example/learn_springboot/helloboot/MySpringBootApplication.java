package org.example.learn_springboot.helloboot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.example.learn_springboot.config.Config;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //class, interface, enum 의 대상에 지정할때는 type
@Configuration
@ComponentScan
@Import(Config.class)
public @interface MySpringBootApplication {
}
