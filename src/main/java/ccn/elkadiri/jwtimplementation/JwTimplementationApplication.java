package ccn.elkadiri.jwtimplementation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JwTimplementationApplication {

    public static void main(String[] args) {

        SpringApplication.run(JwTimplementationApplication.class, args);
        System.out.println("===========================================");
        System.out.println("🚀 Application JWT démarrée avec succès !");
        System.out.println("📍 URL: http://localhost:8080");
        System.out.println("===========================================");

    }

}
