package ru.etozhealexis.districtsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@SpringBootApplication
public class DistrictsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistrictsServiceApplication.class, args);
    }

}
