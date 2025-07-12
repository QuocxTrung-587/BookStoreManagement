package vn.tdtu.finalterm;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Final-Term Project",
				version = "1.0.0",
				description = "This API use for Final-Term Project"
		)
)
public class FinaltermApplication {
	public static void main(String[] args) {
		SpringApplication.run(FinaltermApplication.class, args);
	}
}
