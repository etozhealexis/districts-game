package ru.etozhealexis.telegrambotservice.util;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboardMarkupUtil {

    public InlineKeyboardMarkup generateMergeRequestMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton confirmActionButton = new InlineKeyboardButton();
        confirmActionButton.setText(InlineKeyboardCallback.CONFIRM_ACTION.getText());
        confirmActionButton.setCallbackData(InlineKeyboardCallback.CONFIRM_ACTION.getData());

        InlineKeyboardButton declineActionButton = new InlineKeyboardButton();
        declineActionButton.setText(InlineKeyboardCallback.DECLINE_ACTION.getText());
        declineActionButton.setCallbackData(InlineKeyboardCallback.DECLINE_ACTION.getData());

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(confirmActionButton);
        row1.add(declineActionButton);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row1);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }
}
