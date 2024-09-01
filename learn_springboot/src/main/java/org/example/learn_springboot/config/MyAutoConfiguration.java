package org.example.learn_springboot.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Configuration
// 어떤 클래스에 붙이면 Configuration 붙인것과 같은 효과
public @interface MyAutoConfiguration {
	// 자동 구성방식에 사용할 Configuration 의 목록을 넣을 것
}
