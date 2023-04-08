package ru.nsu.ccfit.msmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MsManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsManagerApplication.class, args);
    }
}
