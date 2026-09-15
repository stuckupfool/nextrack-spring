package com.interviews.nextrack.gui;

import java.util.function.Consumer;

import org.springframework.shell.jline.tui.component.view.TerminalUI;
import org.springframework.shell.jline.tui.component.view.control.BoxView;
import org.springframework.shell.jline.tui.component.view.control.ButtonView;
import org.springframework.shell.jline.tui.component.view.control.GridView;
import org.springframework.shell.jline.tui.component.view.event.KeyHandler;
import org.springframework.shell.jline.tui.component.view.event.KeyEvent.Key;
import org.springframework.shell.jline.tui.component.view.screen.Color;

import com.interviews.nextrack.service.TaskService;
import com.interviews.nextrack.todo.Task;

public class FindDialog extends NexTrackDialog {
    private TaskService taskService;
    private Consumer<Task> onFound;

    public FindDialog(Consumer<Task> onFound, TaskService taskService, GridView view, ButtonView... buttonViews) {
        super(view, buttonViews);
        this.onFound = onFound;
        this.taskService = taskService;
    }

    private static GridView createContent(InputView idInput, String message) {
        final String resolvedMessage = (message == null || message.isEmpty())
                ? "Please enter the ID of the todo task to find."
                : message;
        GridView content = new GridView();
        content.setColumnSize(24);

        BoxView label = new BoxView();
        label.setDrawFunction((screen, rect) -> {
            screen.writerBuilder().layer(1).build().text(resolvedMessage, rect.x() + 5, rect.y() + 1);
            return rect;
        });

        content.addItem(label, 0, 0,
                1, 8,
                0, 0);
        content.addItem(idInput, 1, 0,
                1, 8,
                0, 0);
        return content;
    }

    public static FindDialog construct(Consumer<Task> onFound, TerminalUI ui, TaskService taskService) {
        return construct(onFound, ui, taskService, null);
    }

    public static FindDialog construct(Consumer<Task> onFound, TerminalUI ui, TaskService taskService,
            String message) {
        InputView idInput = GuiFactory.createInput("Task ID", true, "");
        ui.configure(idInput);

        ButtonView findButton = GuiFactory.createDialogButton("Find", Color.STEELBLUE, false);
        ui.configure(findButton);
        ButtonView cancelButton = GuiFactory.createDialogButton("Cancel", Color.STEELBLUE, false);
        ui.configure(cancelButton);

        GridView content = FindDialog.createContent(idInput, message);
        FindDialog dialog = new FindDialog(onFound, taskService, content, findButton, cancelButton);

        findButton.setAction(() -> {
            String id = dialog.getInput("id").getInputText();
            Task task = taskService.getTask(Long.parseLong(id));
            onFound.accept(task);
        });
        cancelButton.setAction(() -> {
            ui.setModal(null);
            TaskGui.quitTerminal(ui);
        });
        
        dialog.registerInput("id", idInput);
        dialog.registerInput("find", findButton);
        dialog.registerInput("cancel", cancelButton);
        ui.configure(dialog);
        return dialog;
    }

    @Override
    public KeyHandler getHotKeyHandler() {
        return args -> {
            int keyCode = args.event().key();
            if (keyCode == Key.Enter) {
                String id = getInput("id").getInputText();
                Task task = taskService.getTask(Long.parseLong(id));
                onFound.accept(task);
            }
            return super.getHotKeyHandler().handle(args);
        };
    }
}
