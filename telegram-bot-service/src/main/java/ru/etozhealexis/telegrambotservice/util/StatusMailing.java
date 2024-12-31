package ru.etozhealexis.telegrambotservice.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusMailing {
    SUCCESS("Успешно"),
    ERROR("Ошибка"),
    PENDING("В ожидании");

    private final String value;
}

