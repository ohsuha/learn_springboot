package org.example.learn_springboot.config.autoconfig;

import org.example.learn_springboot.config.MyAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

@MyAutoConfiguration
public class DispatcherServletConfig {
	@Bean
	public DispatcherServlet dispatcherServlet() {
		return new DispatcherServlet();
	}
}
