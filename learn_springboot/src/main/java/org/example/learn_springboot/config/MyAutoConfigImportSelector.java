package org.example.learn_springboot.config;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyAutoConfigImportSelector implements DeferredImportSelector {
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[] {
			"org.example.learn_springboot.config.autoconfig.DispatcherServletConfig",
			"org.example.learn_springboot.config.autoconfig.TomcatWebServerConfig"
		};
	}
}
