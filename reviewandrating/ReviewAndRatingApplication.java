package com.krenai.reviewandrating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ReviewAndRatingApplication {

	public static void main(String[] args) {

		SpringApplication.run(ReviewAndRatingApplication.class, args);
	}

}
