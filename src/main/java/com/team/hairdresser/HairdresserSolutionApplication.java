package com.team.hairdresser;

import com.team.hairdresser.utils.abstracts.BaseRepositoryCustomImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(value = {"com.team.hairdresser"}, repositoryBaseClass = BaseRepositoryCustomImpl.class)
@EnableScheduling
public class HairdresserSolutionApplication {

    public static void main(String[] args) {
        SpringApplication.run(HairdresserSolutionApplication.class, args);
    }

}
