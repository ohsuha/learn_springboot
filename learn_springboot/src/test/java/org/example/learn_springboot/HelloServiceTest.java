package org.example.learn_springboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelloServiceTest {
	@Test
	void helloServiceTest() {
		SimpleHelloService simpleHelloService = new SimpleHelloService();

		String ret = simpleHelloService.sayHello("Test");

		Assertions.assertThat(ret).isEqualTo("Hello Test");
	}

	@Test
	void helloDecorator() {
		HelloDecorator helloDecorator = new HelloDecorator(name -> name);
		String ret = helloDecorator.sayHello("Test");
		Assertions.assertThat(ret).isEqualTo("*Test*");
	}
}