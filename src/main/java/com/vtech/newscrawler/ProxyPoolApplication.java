package com.vtech.newscrawler;

		import org.springframework.boot.SpringApplication;
		import org.springframework.boot.autoconfigure.SpringBootApplication;
		import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("com.vtech.newscrawler.listener")
public class ProxyPoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyPoolApplication.class, args);
	}
}
