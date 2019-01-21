package com.test.ecommerce;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
public class RestApiTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiTestApplication.class, args);
	}

	
	@Bean(name = "rgbColor")
	public static PropertiesFactoryBean mapper() {
	        PropertiesFactoryBean bean = new PropertiesFactoryBean();
	        bean.setLocation(new ClassPathResource(
	                "rgbColor.properties"));
	        return bean;
	}
}

