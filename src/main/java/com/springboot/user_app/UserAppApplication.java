package com.springboot.user_app;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication
public class UserAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAppApplication.class, args);
	}

	 @Bean
	 public ModelMapper getModelMapper() {
	    return new ModelMapper();
	 }
	 @Bean
	 public OpenAPI getOpenApiBean() {
		 return new OpenAPI()
				 .info(new Info()
						 .title("User management API")
						 .version("1.0.0")
						 .description("This API is used to manage UserOperation like create, update, find, delete")
						 .termsOfService("http://example.com/terms")
						 .contact(new Contact()
								 .name("Deepthi")
								 .email("deepthi@gmail.com")
								 .url("http://your-company.com"))
						 .license(new License()
								 .name("Apache 2.0")
								 .url("https://www.apache.org/license/LICENSE-2.0"))
						 );
						 
	 }

}
