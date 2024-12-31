package ru.etozhealexis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ActionDto {
    private Long userId;
    private String messageText;
    private Integer messageId;
    private ActionType actionType;
    private LocalDateTime time;
}
