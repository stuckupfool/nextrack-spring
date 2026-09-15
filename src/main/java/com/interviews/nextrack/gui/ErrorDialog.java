package com.interviews.nextrack.gui;

import org.springframework.shell.jline.tui.component.view.TerminalUI;
import org.springframework.shell.jline.tui.component.view.control.BoxView;
import org.springframework.shell.jline.tui.component.view.control.ButtonView;
import org.springframework.shell.jline.tui.component.view.control.DialogView;
import org.springframework.shell.jline.tui.component.view.control.View;
import org.springframework.shell.jline.tui.component.view.event.KeyHandler;
import org.springframework.shell.jline.tui.component.view.event.KeyEvent.Key;
import org.springframework.shell.jline.tui.component.view.screen.Color;
import org.springframework.shell.jline.tui.component.view.screen.Screen.Writer;
import org.springframework.shell.jline.tui.geom.HorizontalAlign;
import org.springframework.shell.jline.tui.geom.VerticalAlign;

public class ErrorDialog extends DialogView {
    private TerminalUI ui;
    private ButtonView closeButton;

    public ErrorDialog(View view, ButtonView closeButton, TerminalUI ui) {
        super(view, closeButton);
        this.closeButton = closeButton;
        this.ui = ui;
    }

    private static BoxView createContent(TerminalUI ui, String message) {
        BoxView content = new BoxView();
        content.setDrawFunction((screen, rect) -> {
            Writer writer = screen.writerBuilder().layer(1).build();
            writer.text(message, rect, HorizontalAlign.CENTER, VerticalAlign.CENTER);
            return rect;
        });
        content.setTitle("Error");
        content.setShowBorder(true);
        content.setFocusedTitleColor(Color.RED);
        return content;
    }

    public static ErrorDialog construct(TerminalUI ui, String message) {
        BoxView content = ErrorDialog.createContent(ui, message);
        ButtonView closeButton = GuiFactory.createDialogButton("Close", Color.RED, true);
        closeButton.setAction(() -> {
            TaskGui.quitTerminal(ui);
        });
        ErrorDialog dialog = new ErrorDialog(content, closeButton, ui);
        ui.configure(dialog);
        return dialog;
    }

    @Override
    public KeyHandler getKeyHandler() {
        return closeButton.getKeyHandler();
    }

    @Override
    public KeyHandler getHotKeyHandler() {
        return args -> {
            int keyCode = args.event().key();
            if(keyCode == Key.Enter) {
                TaskGui.quitTerminal(ui);
            }
            return super.getHotKeyHandler().handle(args);
        };
    }
}
