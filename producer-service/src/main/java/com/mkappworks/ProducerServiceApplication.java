package com.mkappworks;

import io.grpc.Server;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProducerServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ProducerServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(final Server server) {
        return args -> server.awaitTermination();
    }
}
