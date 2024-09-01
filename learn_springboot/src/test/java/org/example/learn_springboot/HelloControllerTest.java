package org.example.learn_springboot;

import org.assertj.core.api.Assertions;
import learn_springboot.helloboot.HelloController;
import org.junit.jupiter.api.Test;

public class HelloControllerTest {

	@Test
	void helloControllerTest() {
		HelloController helloController	= new HelloController(name -> name);
		// DI는 두개의 의존 관계가 있는 객체를 스프링 제3자인 어셈블러가 런타임시에 주입을 통해 관계를 맺어주는 것

		String result = helloController.hello("suah");

		Assertions.assertThat(result).isEqualTo("suah");
	}

	@Test
	void helloControllerTest2() {
		HelloController helloController	= new HelloController(name -> name);

		Assertions.assertThatThrownBy(
			() -> {helloController.hello(null);}
		).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void helloControllerTest3() {
		HelloController helloController	= new HelloController(name -> name);

		Assertions.assertThatThrownBy(
			() -> {helloController.hello("");}
		).isInstanceOf(IllegalArgumentException.class);
	}
}
