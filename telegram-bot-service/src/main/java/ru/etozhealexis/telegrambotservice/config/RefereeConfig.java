package ru.etozhealexis.telegrambotservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "telegram.referee")
@Component
@Getter
@Setter
public class RefereeConfig {
    private String username;
    private Long id;
}
