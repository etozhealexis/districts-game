package ru.etozhealexis.telegrambotservice.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Command {

    SET_DISTRICT("/setdistrict", "назначает район игроку"),
    SET_DISTRICT_ALIAS("/setdistrictalias", "назначает тег району"),
    INVADE("/invade", "начинает вторжение в другой район"),
    DEFEND("/defend", "защищает район");

    private final String alias;
    private final String description;
}
