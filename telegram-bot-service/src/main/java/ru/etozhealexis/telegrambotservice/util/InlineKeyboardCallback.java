package ru.etozhealexis.telegrambotservice.util;

import lombok.Getter;

@Getter
public enum InlineKeyboardCallback {
    CONFIRM_ACTION("Подтвердить", "confirm"),
    DECLINE_ACTION("Отклонить", "decline");

    private final String text;
    private final String data;

    InlineKeyboardCallback(String text, String data) {
        this.text = text;
        this.data = data;
    }
}
