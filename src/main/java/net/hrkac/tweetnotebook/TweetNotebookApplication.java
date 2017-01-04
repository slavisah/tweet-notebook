package net.hrkac.tweetnotebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TweetNotebookApplication {

	public static void main(String[] args) {
	    System.setProperty("spring.profiles.active", "integration-test");
	    System.setProperty("spring.config.location", "profiles/${spring.profiles.active}/config.properties");
		SpringApplication.run(TweetNotebookApplication.class, args);
	}
}
