package com.example.elearningplatform;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import jakarta.transaction.Transactional;
import lombok.Setter;

@SpringBootApplication
@Setter
@EnableJpaRepositories
public class ELearningPlatformApplication
        implements ApplicationRunner {

    /*********************************************************************************** */
    public static void main(String[] args) {
        SpringApplication.run(ELearningPlatformApplication.class, args);

    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("running");
        System.out.println("running");
        System.out.println("running");

        // generateData.truncateDtabase();
        // generateData.createData();
        // generateData.setRelationships();
    }
}

