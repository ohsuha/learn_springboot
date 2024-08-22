package org.example.learn_springboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class LearnSpringbootApplication {

	public static void main(String[] args) {

		TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = null;
		/**
		 * 1. 서블릿 띄우기

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
		 */

		/**
		 * 2. 요청을 front controller 를 통해서 받아서 처리하기

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
		 */
		/**
		 * 3 spring container 사용하기

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

		 */
		/**
		 * 4. helloService 타입의 클래스를 빈으로 등록해서 DI 로 자동으로 주입하게 하자


		 GenericApplicationContext genericApplicationContext= new GenericApplicationContext();
		 genericApplicationContext.registerBean(HelloController.class);
		 // genericApplicationContext.registerBean(HelloService.class); // 인터페이스지 클래스가 아님
		 genericApplicationContext.registerBean(SimpleHelloService.class);
		 //초창기에는 xml으로 빈을 지정해서 생성자에 어떤 빈을 주입할지 정보도 다 기술을 해줬어야했다

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

		 */

		/**
		 * 5. 서블릿 컨테이너나 그런 것들을 다루지 않고 개발 하고싶다~
		 * 맵핑과 특정 컨트롤러로 바인딩해 보내주는 역할을 서블릿 코드 안에서 하고 있다.
		 * 디스패쳐 서블릿을 통해 개발해보자
		 */

		GenericWebApplicationContext genericWebApplicationContext = new GenericWebApplicationContext();
		genericWebApplicationContext.registerBean(HelloController.class);
		genericWebApplicationContext.registerBean(SimpleHelloService.class);
		//dispatcher servlet 은 GenericWebApplicationContext 를 사용해야한다.

		genericWebApplicationContext.refresh();

		webServer = serverFactory.getWebServer(servletContext -> {
			servletContext.addServlet("dispatcherServlet",
				new DispatcherServlet(genericWebApplicationContext)
				//디스패쳐 서블릿에게 어떤 요청이 어떤 빈을 처리하라고 알려줘야함
				// 1. xml , 2. mapping 정보를 처리할 컨트롤러 클래스 안에 코드로 맵핑 정보를 넣는 것.
				// 2번은 컨트롤러에 맵핑 정보를 추가해준다. @GetMapping
			).addMapping("/*");
		});

		/**
		 * 6. 스프링 컨테이너로 통합
		 */


		webServer.start();
	}
}
