package org.example.learn_springboot;

import java.util.Objects;

public class HelloController {

	private final HelloService helloService;
	//final 이니까 생성자나 어디서나 초기화가 필요하다고 컴파일 에러가 발생한다.

	public HelloController(HelloService helloService) {
		this.helloService = helloService;
	}

	public String hello(String name) {
		//SimpleHelloService simpleHelloService = new SimpleHelloService();
		//직접 인스턴스를 만들지 않고 어셈블러에 의해 헬로 컨트롤러 클래스의 객체를 만들때 생성자 파라미터로 주입할 수 있도록 변경 하자

		return helloService.sayHello(Objects.requireNonNull(name));
		// 만약 null 이면 예외를 던진다.
		// null 인 경우를 방지 하고 null 이 아닐 때만 사용 가능
	}
}
