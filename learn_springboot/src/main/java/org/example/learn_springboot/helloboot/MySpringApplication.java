package org.example.learn_springboot.helloboot;

import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class MySpringApplication {

	public static void run(Class<?> applicationClass, String... args) {
		AnnotationConfigWebApplicationContext annotationConfigWebApplicationContext = new AnnotationConfigWebApplicationContext() {
			// 어노테이션이 붙은 것들을 이용해 구성 정보를 가져오는 클래스

			@Override
			protected void onRefresh() {
				super.onRefresh();

				ServletWebServerFactory servletFactory = this.getBean(ServletWebServerFactory.class);
				DispatcherServlet dispatcherServlet = this.getBean(DispatcherServlet.class);
				// dispatcherServlet.setApplicationContext(this);
				// 애플리케이션 컨텍스트를 지정해주지 않아도 잘 동작한다. 디스패쳐 서블릿이 어떻게 컨트롤러를 찾아 맵핑 정보를 찾는지?
				// 스프링 컨테이너가 디스패쳐 서블릿은 애플리케이션 컨텍스트가 필요하구나 하고 알아서 주입을 해준다.
				// ApplicationContextAware 인터페이스를 구현한 클래스가 스프링에 빈으로 등록이 되면
				// 스프링 컨테이너는 인터페이스의 setter 를 통해서 주입해 줌
				WebServer webServer = servletFactory.getWebServer(servletContext -> {
					servletContext.addServlet("dispatcherServlet", dispatcherServlet)
						.addMapping("/*");
				});
				webServer.start();
			}
		};
		// genericWebApplicationContext.registerBean(HelloController.class); //필요 없어짐, 빈을 어노테이션으로 등록하므로
		// genericWebApplicationContext.registerBean(SimpleHelloService.class);
		annotationConfigWebApplicationContext.register(applicationClass);
		annotationConfigWebApplicationContext.refresh();
	}


}
