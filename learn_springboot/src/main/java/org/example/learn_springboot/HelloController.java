package org.example.learn_springboot;

import java.util.Objects;

public class HelloController {
	public String hello(String name) {
		SimpleHelloService simpleHelloService = new SimpleHelloService();

		return simpleHelloService.sayHello(Objects.requireNonNull(name));
		// 만약 null 이면 예외를 던진다.
		// null 인 경우를 방지 하고 null 이 아닐 때만 사용 가능
	}
}
