package org.bot.abyssal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.bot")
public class AbyssalApplication {
    public static void main(String[] args) {
        SpringApplication.run(AbyssalApplication.class, args);
    }
}
