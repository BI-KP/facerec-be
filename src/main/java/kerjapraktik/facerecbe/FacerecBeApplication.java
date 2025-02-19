package kerjapraktik.facerecbe;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FacerecBeApplication {

    public static void main(String[] args) {
        Dotenv env = Dotenv.load();
        System.setProperty("spring.datasource.url", env.get("DB_URL"));
        System.setProperty("spring.datasource.username", env.get("DB_USER"));
        System.setProperty("spring.datasource.password", env.get("DB_PASSWORD"));
        SpringApplication.run(FacerecBeApplication.class, args);
    }

}
