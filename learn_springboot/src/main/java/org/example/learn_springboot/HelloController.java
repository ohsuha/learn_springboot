package org.example.learn_springboot;

import java.util.Objects;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController // 5. dispatcher 서블릿과는 관련이 없다., 이게 붙으면 아래의 모든 메서드가 @ResponseBody 가 붙여져 있다고 하는 것과 같다.
//@RequestMapping // 5. 디스패쳐 서블릿이 모든 메소드를 다 뒤지면 느리니까 클래스에 컨트롤러로 명시해 줘서 빠르게 찾도록
public class HelloController implements ApplicationContextAware {

	private final HelloService helloService;
	//final 이니까 생성자나 어디서나 초기화가 필요하다고 컴파일 에러가 발생한다.

	public HelloController(HelloService helloService) {
		this.helloService = helloService;
	}

	@GetMapping("/hello") // 5. 이 어노테이션을 넣으면 디스패쳐 서블릿이 빈을 모두 뒤져서 웹 요청을 처리할 수 있는 맵핑 정보를 가지고 있느 클래스를 찾아서
	// 위 어노테이션이 있으면 웹 요청을 처리할 수 있구나 해서 요청 정보와 맵핑 정보를 맞춰 맵핑 테이블을 만든다.
	// 만들어둔 맵핑 테이블을 통해 요청을 처리한다.
	// 그런데
	// @RequestMapping(value = "/hello", method = RequestMethod.GET) : 옛날에 쓰던 방식
	// @ResponseBody
	public String hello(String name) {
		//SimpleHelloService simpleHelloService = new SimpleHelloService();
		//직접 인스턴스를 만들지 않고 어셈블러에 의해 헬로 컨트롤러 클래스의 객체를 만들때 생성자 파라미터로 주입할 수 있도록 변경 하자

		// return helloService.sayHello(Objects.requireNonNull(name));
		// 만약 null 이면 예외를 던진다.
		// null 인 경우를 방지 하고 null 이 아닐 때만 사용 가능

		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException();
		}

		return helloService.sayHello(name);

		// 5. 디스패쳐 서블릿은 리턴된 String을 보고 view를 리턴하라고 하기 때문에 view 를 찾는다.
		//    원하는 작업에 맞는 view 가 없으니 404에러, 결과 String 을 그대로 바디에 넣어서 응답하기 위해서는
		//    @ResponseBody
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println(applicationContext);
	}
}
