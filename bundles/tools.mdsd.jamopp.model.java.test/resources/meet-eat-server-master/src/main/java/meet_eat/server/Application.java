package meet_eat.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Represents the entry point and main class of this application.
 */
@SpringBootApplication
public class Application {

    /**
     * Starts and initializes the Spring Boot server.
     *
     * @param args arguments used to initialize the server application in a specific manner
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
