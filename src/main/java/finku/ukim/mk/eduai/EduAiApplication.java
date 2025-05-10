package finku.ukim.mk.eduai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class EduAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduAiApplication.class, args);
	}

}
