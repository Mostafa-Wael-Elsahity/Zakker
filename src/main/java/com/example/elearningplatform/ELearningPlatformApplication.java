package com.example.elearningplatform;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.elearningplatform.user.role.Role;
import com.example.elearningplatform.user.role.RoleRepository;

import jakarta.transaction.Transactional;
import lombok.Setter;

@SpringBootApplication
@Setter
@EnableJpaRepositories
public class ELearningPlatformApplication
        implements ApplicationRunner {
    // @Autowired
    // private RoleRepository roleRepository;

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



        // Category category1 = new Category();
        // category1.setName("Development");
        // categoryRepository.save(category1);
        // Category category2 = new Category();
        // category2.setName("Design");
        // categoryRepository.save(category2);

        // Category category3 = new Category();
        // category3.setName("Marketing");

        // categoryRepository.save(category3);

        // Category category4 = new Category();
        // category4.setName("Business");

        // categoryRepository.save(category4);
        // Category category5 = new Category();
        // category5.setName("Photography");
        // categoryRepository.save(category5);
        // Category category6 = new Category();
        // category6.setName("Music");
        // categoryRepository.save(category6);
        // Category category7 = new Category();
        // category7.setName("Lifestyle");
        // categoryRepository.save(category7);
        // Category category8 = new Category();
        // category8.setName("Health");
        // categoryRepository.save(category8);

        // generateData.truncateDtabase();
        // generateData.createData();
        // generateData.setRelationships();
    }
}

