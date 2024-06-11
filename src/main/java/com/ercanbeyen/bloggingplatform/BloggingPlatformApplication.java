package com.ercanbeyen.bloggingplatform;

import com.ercanbeyen.bloggingplatform.entity.Ticket;
import com.ercanbeyen.bloggingplatform.util.EmailUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.apache.ibatis.type.MappedTypes;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Blogging Platform",
				version = "1.0",
				description = "Blog API",
				contact = @Contact(
						name = "Blog API Support",
						email = EmailUtil.SUPPORT_EMAIL
				),
				license = @License(
						name = "Apache 2.0",
						url = "http://www.apache.org/licenses/LICENSE-2.0.html"
				)
		)
)
@MapperScan("com.ercanbeyen.bloggingplatform.mapper")
@MappedTypes({Ticket.class})
public class BloggingPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(BloggingPlatformApplication.class, args);
	}

}
