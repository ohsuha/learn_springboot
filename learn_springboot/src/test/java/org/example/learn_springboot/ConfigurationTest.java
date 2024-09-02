package org.example.learn_springboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class ConfigurationTest {

	@Test
	void configurationTest() {
		MyConfig myConfig = new MyConfig();
		Bean1 bean1 = myConfig.bean1();
		Bean2 bean2 = myConfig.bean2();

		// Assertions.assertThat(bean1).isSameAs(bean2);
		// 다르다고 나온다.

		AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
		ac.register(MyConfig.class);
		ac.refresh();

		Bean1 bean3 = ac.getBean(Bean1.class);
		Bean2 bean4 = ac.getBean(Bean2.class);

		Assertions.assertThat(bean3.common).isSameAs(bean4.common);
		// 동일하다고 나온다.
		// @Configuration 이라는 어노테이션이 붙은 것을 스프링내의 환경에서는 동작 방식이 달라진다.
	}

	@Configuration
	static class MyConfig {
		@Bean
		Common common() {
			return new Common();
		}

		@Bean
		Bean1 bean1() {
			return new Bean1(common());
		}

		@Bean
		Bean2 bean2() {
			return new Bean2(common());
		}
	}

	static class Bean1 {
		private final Common common;

		Bean1(Common common) {
			this.common = common;
		}
	}

	static class Bean2 {
		private final Common common;

		Bean2(Common common) {
			this.common = common;
		}
	}

	static class Common {

	}

	static class MyConfigProxy extends MyConfig {
		private Common common;

		@Override
		Common common() {
			if (this.common == null) {
				common = super.common(); // null 이면 새로 생성하고
			}
			return common; // 이 필드에 저장해둔 common 을 리턴한다.
		}
	}

	@Test
	void proxyCommonMethod() {
		MyConfigProxy myConfigProxy = new MyConfigProxy();
		Bean1 bean1 = myConfigProxy.bean1();
		Bean2 bean2 = myConfigProxy.bean2();

		Assertions.assertThat(bean1.common).isSameAs(bean2.common);
	}
}
