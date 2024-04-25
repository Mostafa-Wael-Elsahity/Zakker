package com.example.elearningplatform;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class ELearningPlatformApplication
        implements ApplicationRunner {
    private final GenerateData generateData;
    /*********************************************************************************** */
    public static void main(String[] args) {
        SpringApplication.run(ELearningPlatformApplication.class, args);

    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Hello World from Application Runner");
        // generateData.truncateDtabase();
        // generateData.createData();
        // generateData.setRelationships();



    }
}

