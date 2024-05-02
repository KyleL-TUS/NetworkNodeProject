package org.ericsson.miniproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class NetworkNodeAPI {

    public static void main(String[] args) {
        SpringApplication.run(NetworkNodeAPI.class, args);
        log.info("Network Node API Micro Service running...");
    }
}
