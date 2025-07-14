package finku.ukim.mk.eduai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(
		exclude = { DataSourceAutoConfiguration.class },
		scanBasePackages = "finku.ukim.mk.eduai"
)
@EnableAsync
@EntityScan(basePackages = "finku.ukim.mk.eduai.model")
public class EduAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduAiApplication.class, args);
	}

}
