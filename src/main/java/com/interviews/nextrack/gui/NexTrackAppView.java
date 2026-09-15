package com.interviews.nextrack.gui;

import org.springframework.shell.jline.tui.component.view.control.AppView;
import org.springframework.shell.jline.tui.component.view.control.View;
import org.springframework.shell.jline.tui.component.view.event.KeyHandler;
import org.springframework.shell.jline.tui.component.view.TerminalUI;

public class NexTrackAppView extends AppView {
    private TerminalUI ui;

    public NexTrackAppView(
            View main,
            View menuBar,
            View statusBar,
            TerminalUI ui) {

        super(main, menuBar, statusBar);
        this.ui = ui;
    }

    @Override
    public KeyHandler getKeyHandler() {
        return args -> {

            View modal = ui.getModal();

            if (modal != null) {

                View focused = modal;

                KeyHandler handler = focused.getKeyHandler();

                if (handler != null) {
                    return handler.handle(args);
                }

                return KeyHandler.resultOf(
                        args.event(),
                        true,
                        null);
            }

            return super.getKeyHandler().handle(args);
        };
    }

    @Override
    public KeyHandler getHotKeyHandler() {
        return args -> {
            View modal = ui.getModal();
            if (modal != null) {

                // Modal is active. Do not allow AppView
                // hotkeys to operate on the underlying application.
                return modal.getHotKeyHandler().handle(args);
            }

            return super.getHotKeyHandler().handle(args);
        };
    }
}