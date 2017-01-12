package net.hrkac.tweetnotebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;

@SpringBootApplication(exclude = WebMvcAutoConfiguration.class)
public class TweetNotebookApplication {

	public static void main(String[] args) {
	    System.setProperty("spring.profiles.active", "dev");
	    System.setProperty("spring.config.location", "profiles/${spring.profiles.active}/config.properties");
		SpringApplication.run(TweetNotebookApplication.class, args);
	}
}
