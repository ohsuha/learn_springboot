package learn_springboot.helloboot;

@MyComponent
public class SimpleHelloService implements HelloService {
	@Override
	public String sayHello(String name) {
		return "Hello " + name;
	}
}
