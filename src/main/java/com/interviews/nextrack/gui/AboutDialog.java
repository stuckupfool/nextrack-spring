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

public class AboutDialog extends DialogView {
    private ButtonView closeButton;

    public AboutDialog(View view, ButtonView closeButton) {
        super(view, closeButton);
        this.closeButton = closeButton;
    }

    private static BoxView createContent(TerminalUI ui) {
        BoxView content = new BoxView();
        content.setDrawFunction((screen, rect) -> {
            String text = "NexTrack - Track Your Next Task";
            Writer writer = screen.writerBuilder().layer(1).build();
            writer.text(text, rect.x() + ((rect.width())/2) - (text.length()/2), rect.y() + 2);
            text = "Version 1.0";
            writer.text(text, rect.x() + ((rect.width())/2) - (text.length()/2), rect.y() + 4);
            return rect;
        });
        content.setTitle("About");
        content.setShowBorder(true);
        content.setFocusedTitleColor(Color.STEELBLUE);
        return content;
    }

    public static AboutDialog construct(TerminalUI ui) {
        BoxView content = AboutDialog.createContent(ui);
        ButtonView closeButton = GuiFactory.createDialogButton("Close", () -> {
            ui.setModal(null);
        }, Color.STEELBLUE, true);
        AboutDialog dialog = new AboutDialog(content, closeButton);
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
            if (keyCode == Key.Enter) {
                getViewService().setModal(null);
            }
            return super.getHotKeyHandler().handle(args);
        };
    }
}
