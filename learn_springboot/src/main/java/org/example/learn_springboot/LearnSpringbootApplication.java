package org.example.learn_springboot;

import java.io.IOException;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LearnSpringbootApplication {

	public static void main(String[] args) {

		TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = null;
		/**
		 * 1. 서블릿 띄우기
		 */
		 webServer = serverFactory.getWebServer(servletContext -> {
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

		/**
		 * 2. 요청을 front controller 를 통해서 받아서 처리하기
		 */
		webServer = serverFactory.getWebServer(servletContext -> {
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

						// resp.setStatus(HttpStatus.OK.value());
						// resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);

						resp.setContentType(MediaType.TEXT_PLAIN_VALUE);
						resp.getWriter().println(result);
					} else if (req.getRequestURI().equals("/user")){
						// ...
					} else {
						resp.setStatus(HttpStatus.NOT_FOUND.value());
					}
				}
			}).addMapping("/*"); // 모든 요청에 대해 처리를 하겠다는 와일드 카드
		});

		/**
		 * 3 spring container 사용하기
		 */
		// spring container를 만들고
		GenericApplicationContext genericApplicationContext= new GenericApplicationContext();
		// 어떤 클래스를 이용해 bean 메타 정보를 만들 것인가가 필요함
		genericApplicationContext.registerBean(HelloController.class);
		// bean 을 어떤 클래스로 만들것인가 등등의 정보를 등록함
		genericApplicationContext.refresh();

		webServer= serverFactory.getWebServer(servletContext -> {
		servletContext.addServlet("hello", new HttpServlet() {
			protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				if(req.getRequestURI().equals("/hello") && req.getMethod().equals("GET")) {
					String name = req.getParameter("name");
					HelloController helloController = genericApplicationContext.getBean(HelloController.class);
					String result = helloController.hello(name);

					resp.setContentType(MediaType.TEXT_PLAIN_VALUE);
					resp.getWriter().println(result);
				}
			}
		}).addMapping("/hello");
		});

		webServer.start();
	}
}
