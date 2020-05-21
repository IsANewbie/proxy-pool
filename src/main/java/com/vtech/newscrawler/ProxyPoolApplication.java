package com.vtech.newscrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
//@ServletComponentScan("com.vtech.newscrawler.listener")
//public class ProxyPoolApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(ProxyPoolApplication.class, args);
//	}
//}

public class ProxyPoolApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ProxyPoolApplication.class);
	}

	public static void main(String[] args) {
		System.out.println();
		SpringApplication.run(ProxyPoolApplication.class, args);
	}
}
