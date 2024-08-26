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
	void helloServiceTest2() {
		HelloController helloController	= new HelloController(name -> name);

		String result = helloController.hello("suah");

		Assertions.assertThat(result).isEqualTo("Hello suah");
	}

	@Test
	void helloServiceTest3() {
		HelloController helloController	= new HelloController(name -> name);

		Assertions.assertThatThrownBy(
			() -> {helloController.hello(null);}
		).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void helloServiceTest4() {
		HelloController helloController	= new HelloController(name -> name);

		Assertions.assertThatThrownBy(
			() -> {helloController.hello("");}
		).isInstanceOf(IllegalArgumentException.class);
	}
}
