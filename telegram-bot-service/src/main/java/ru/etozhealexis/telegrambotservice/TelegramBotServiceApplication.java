package ru.etozhealexis.telegrambotservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@EnableFeignClients
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@SpringBootApplication
public class TelegramBotServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotServiceApplication.class, args);
    }

}
