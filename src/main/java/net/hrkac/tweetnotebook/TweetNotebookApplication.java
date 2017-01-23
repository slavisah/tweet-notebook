package net.hrkac.tweetnotebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;

@SpringBootApplication(exclude = WebMvcAutoConfiguration.class)
public class TweetNotebookApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetNotebookApplication.class, args);
	}
}
