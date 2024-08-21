package org.example.learn_springboot;

import java.io.IOException;

import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LearnSpringbootApplication {

	public static void main(String[] args) {

		TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = serverFactory.getWebServer(servletContext -> {
			servletContext.addServlet("hello", new HttpServlet() {
				protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
					IOException {
					// 요청을 다루기
					String name = req.getParameter("name");

					// web 응답의 3가지 요소
					resp.setStatus(HttpStatus.OK.value());
					resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
					resp.getWriter().println("Hello " + name);
				}
			}).addMapping("/hello"); // /hello 로 들어오는 요청에 대해
		}); //web server, servlet container 를 만드는 메소드

		WebServer frontController = serverFactory.getWebServer(servletContext -> {
			servletContext.addServlet("frontcontroller", new HttpServlet() {
				protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
					IOException {
					// 인증, 보안, 다국어, 공통기능
					// 기존에는 URL 을 결정해서 맵핑하는걸 서블릿이 했지만 프론트 컨트롤러가 담당해야한다.
					HelloController helloController = new HelloController();

					if(req.getRequestURI().equals("/hello") && req.getMethod().equals("GET")) {
						// 요청을 다루기
						String name = req.getParameter("name");
						// hello controller 에서 다루는 로직은 다른 클래스에서
						String result = helloController.hello(name);
						resp.setStatus(HttpStatus.OK.value());
						resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
						resp.getWriter().println(result);
					} else if (req.getRequestURI().equals("/user")){
						// ...
					} else {
						resp.setStatus(HttpStatus.NOT_FOUND.value());
					}
				}
			}).addMapping("/*"); // 모든 요청에 대해 처리를 하겠다는 와일드 카드
		});

		frontController.start();
	}
}
