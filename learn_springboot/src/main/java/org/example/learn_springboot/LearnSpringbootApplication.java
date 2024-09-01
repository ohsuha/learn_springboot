package org.example.learn_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

// @ComponentScan // 8. 컴포넌트가 붙은 클래스를 찾아서 등록해달라
// @Configuration // 7. 스프링 컨테이너가 빈 오브젝트를 가진 클래스라는 것을 인지해야 할때 붙여줌, 구성정보를 가지고 있는 클래스다
@MySpringBootAnnotation
public class LearnSpringbootApplication {

	public static void main(String[] args) {
		/**
		TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();

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
		 */
		/**
		 * 6. 스프링 컨테이너로 통합
		 * 스프링 컨테이늘 생성하고 빈을 초기화 하는 작업과, 서블릿 컨테이너를 만들고 필요한 디스패쳐 서블릿을 만드는 서블릿 초기화 작업
		 * 이 두가지로 이뤄졌었는데 이걸 스프링 컨테이너가 초기화 되는 과정중에 모두 일어나도록 수정해 보자


		 GenericWebApplicationContext genericWebApplicationContext = new GenericWebApplicationContext() {

		 //스프링 컨테이너를 초기화 하는 중에 부가적인 작업이 있다면 overriding 해서 hook 메소드를 추가하자
		 // 추가할때 다음과 같이 익명 클래스를 사용할 수 있다.
		 @Override protected void onRefresh() {
		 super.onRefresh();

		 ServletWebServerFactory servletFactory = new TomcatServletWebServerFactory();

		 WebServer dispatcherServlet = servletFactory.getWebServer(servletContext -> {
		 servletContext.addServlet("dispatcherServlet",
		 new DispatcherServlet(this) //this = genericWebApplicationContext
		 ).addMapping("/*");
		 });
		 dispatcherServlet.start();
		 }
		 };
		 genericWebApplicationContext.registerBean(HelloController.class);
		 genericWebApplicationContext.registerBean(SimpleHelloService.class);
		 genericWebApplicationContext.refresh();
		 */

		/** 7. bean 을 등록해서 스프링 컨테이너 내의 bean 이 다른 컴포넌트를 사용한다면 어느시점에 의존시켜줄 것인가를 설정해줄 수 있다.
		 * 팩토리 메소드를 사용해서 빈 오브젝트를 다 생성하고 의존관계 주입도 다 하고 리턴하는 오브젝트를 스프링 컨테이너에게 빈으로 등록해서 나중에 사용해 하면 되는 것
		 * 어떤 경우에는 빈 오브젝트를 만들고 초기화 하는 작업이 복잡할때가 있다. 이 복잡한 설정을 자바 코드로 만들면 간결해진다.

		 AnnotationConfigWebApplicationContext annotationConfigWebApplicationContext = new AnnotationConfigWebApplicationContext() {
		 // 어노테이션이 붙은 것들을 이용해 구성 정보를 가져오는 클래스

		 @Override protected void onRefresh() {
		 super.onRefresh();

		 ServletWebServerFactory servletFactory = new TomcatServletWebServerFactory();

		 WebServer dispatcherServlet = servletFactory.getWebServer(servletContext -> {
		 servletContext.addServlet("dispatcherServlet",
		 new DispatcherServlet(this) //this = genericWebApplicationContext
		 ).addMapping("/*");
		 });
		 dispatcherServlet.start();
		 }
		 };
		 // genericWebApplicationContext.registerBean(HelloController.class); //필요 없어짐, 빈을 어노테이션으로 등록하므로
		 // genericWebApplicationContext.registerBean(SimpleHelloService.class);
		 annotationConfigWebApplicationContext.register(LearnSpringbootApplication.class);
		 annotationConfigWebApplicationContext.refresh();
		 */

		/**
		 * 8. Bean 의 생명주기 메소드
		 * 톰캣 서블릿 팩토리와 디스패쳐 서블릿 두개가 있는데 이게 없으면 시작할수가 없다.
		 * 이 두개의 오브젝트도 스프링에 빈으로 등록해서 컨테이너가 이를 관리하게 하자


		 AnnotationConfigWebApplicationContext annotationConfigWebApplicationContext = new AnnotationConfigWebApplicationContext() {
		 // 어노테이션이 붙은 것들을 이용해 구성 정보를 가져오는 클래스

		 @Override protected void onRefresh() {
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
		 annotationConfigWebApplicationContext.register(LearnSpringbootApplication.class);
		 annotationConfigWebApplicationContext.refresh();
		 */

		// MySpringApplication.run(LearnSpringbootApplication.class, args);
		SpringApplication.run(LearnSpringbootApplication.class, args);
	}

	/**
	config 로 이동
	 @Bean
	public ServletWebServerFactory servletWebServerFactory() {
		return new TomcatServletWebServerFactory();
	}

	@Bean
	public DispatcherServlet dispatcherServlet() {
		// 자기가 이용할 컨트롤러를 찾아야해서 스프링 컨테이너를 넘겨줘야한다
		return new DispatcherServlet();
	}
	/

	/**
	 * // 팩토리 메소드
	 *
	 * @Bean //7.스프링에게 빈이라고 알려주기
	 * public HelloController helloController(HelloService helloService) {
	 * return new HelloController(helloService);
	 * }
	 * @Bean public HelloService helloService() {
	 * return new SimpleHelloService(); // 빈을 주입받을때 어떤 타입을 기대하는가?
	 * }
	 */
}
