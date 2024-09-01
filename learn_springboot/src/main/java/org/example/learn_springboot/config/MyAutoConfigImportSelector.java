package org.example.learn_springboot.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyAutoConfigImportSelector implements DeferredImportSelector {
	private final ClassLoader classLoader;

	public MyAutoConfigImportSelector(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		// return new String[] {
		// 	"org.example.learn_springboot.config.autoconfig.DispatcherServletConfig",
		// 	"org.example.learn_springboot.config.autoconfig.TomcatWebServerConfig"
		//
		// 	// 자동 구성 정보의 후보들을 불러오기, 어떤 방식들이 사용될지는 선택하게 하기
		// };
		/**
		Iterable<String> candidates = ImportCandidates.load(MyAutoConfiguration.class, classLoader);
		// classLoader : 어떤 어플리케이션의 클래스 패스에서 리소스를 읽어올때 사용하는것
		// 클래스 로더가 스프링 컨테이너가 빈을 생성하기 위해서 빈정보를 로딩할때 사용하는 클래스를 넣어줘야한다.

		// ImportCandidates에는 자동구성에 사용할 서비스의 목록들이 들어가있다.
		return StreamSupport.stream(candidates.spliterator(), false).toArray(String[]::new);
		**/

		List<String> autoConfigs = new ArrayList<>();
		ImportCandidates.load(MyAutoConfiguration.class, classLoader).forEach(autoConfig -> autoConfigs.add(autoConfig));
		return autoConfigs.toArray(new String[0]);
	}
}
