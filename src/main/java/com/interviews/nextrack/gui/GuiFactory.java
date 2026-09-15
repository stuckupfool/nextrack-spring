package com.interviews.nextrack.gui;

import org.springframework.shell.jline.tui.component.view.control.ButtonView;
import org.springframework.shell.jline.tui.component.view.screen.Color;

public class GuiFactory {
    public static InputView createInput(String label, boolean focused, String text) {
        InputView input = new InputView();
        input.setInputText(text);
        input.setShowBorder(true);
        input.setTitle(label);
        input.setFocusedTitleColor(Color.STEELBLUE);
        input.focus(input, focused);
        input.setLayer(1);
        input.setDrawFunction((screen, rect) -> {
            String inputText = input.getInputText();
            screen.writerBuilder().layer(2).build().text(inputText, rect.x() + 1, rect.y() + 1);
            return rect;
        });

        return input;
    }

    public static ButtonView createDialogButton(String title, int selectedColor, boolean focused) {
        ButtonView button = new ButtonView();
        button.setShowBorder(true);
        button.setLayer(1);
        button.setDrawFunction((screen, rect) -> {
            boolean focus = button.hasFocus();
            screen.writerBuilder()
                .color(focus ? selectedColor : -1)
                .layer(2)
                .build()
                .text(title, rect.x() + 1, rect.y() + 1);
            return rect;
        });
        button.focus(button, focused);
        return button;
    }

    public static ButtonView createDialogButton(String title, Runnable action, int selectedColor, boolean focused) {
        ButtonView button = new ButtonView("", action);
        button.setShowBorder(true);
        button.setLayer(1);
        button.setDrawFunction((screen, rect) -> {
            boolean focus = button.hasFocus();
            screen.writerBuilder()
                .color(focus ? selectedColor : -1)
                .layer(2)
                .build()
                .text(title, rect.x()+1, rect.y() + 1);
	        return rect;
        });
        button.focus(button, focused);
        return button;
    }
}
