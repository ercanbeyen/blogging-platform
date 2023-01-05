package com.ercanbeyen.bloggingplatform;

import com.ercanbeyen.bloggingplatform.constants.Location;
import com.ercanbeyen.bloggingplatform.constants.Gender;
import com.ercanbeyen.bloggingplatform.entity.Comment;
import com.ercanbeyen.bloggingplatform.entity.Person;
import com.ercanbeyen.bloggingplatform.entity.Post;
import com.ercanbeyen.bloggingplatform.repository.CommentRepository;
import com.ercanbeyen.bloggingplatform.repository.PersonRepository;
import com.ercanbeyen.bloggingplatform.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class BloggingPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(BloggingPlatformApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(PersonRepository personRepository, PostRepository postRepository, CommentRepository commentRepository) {
		return args -> {
			Location location = new Location(
					"Turkey",
					"Istanbul"
			);
			String username = "trial_username";
			Person person = new Person(
					"Trial_Name",
					"Trial_Surname",
					username,
					"trial@email.com",
					"trial_about",
					Gender.MALE,
					location,
					List.of("Sport", "Movie"),
					LocalDateTime.now()
			);
			personRepository.save(person);

			Comment comment = new Comment(username, "Trial comment", LocalDateTime.now());
			commentRepository.save(comment);


			Post post = new Post(
					username,
					"Title",
					"Hello World",
					"Movie",
					2,
					List.of("Movie", "2-D", "Science Fiction"),
					List.of(comment),
					LocalDateTime.now()
			);
			postRepository.save(post);
		};
	}

}
