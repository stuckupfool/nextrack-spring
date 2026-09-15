package com.interviews.nextrack.gui;

import org.springframework.shell.jline.tui.component.view.TerminalUI;
import org.springframework.shell.jline.tui.component.view.control.ButtonView;
import org.springframework.shell.jline.tui.component.view.control.GridView;
import org.springframework.shell.jline.tui.component.view.screen.Color;

import com.interviews.nextrack.service.TaskService;
import com.interviews.nextrack.todo.Task;

public class TaskDialog extends NexTrackDialog {
    public TaskDialog(GridView view, ButtonView... buttonViews) {
        super(view, buttonViews);
    }

    public static GridView createContent(InputView titleInput, InputView descriptionInput) {
        GridView content = new GridView();
        content.setColumnSize(24);

        content.addItem(titleInput, 0, 0,
                1, 8,
                0, 0);
        content.addItem(descriptionInput, 1, 0,
                1, 8,
                0, 0);
        return content;
    }

    public static TaskDialog construct(TerminalUI ui, TaskService taskService) {
        return construct(null, ui, taskService);
    }

    public static TaskDialog construct(Task task, TerminalUI ui, TaskService taskService) {
        String titleDefault = task != null ? task.getTitle() : "";
        InputView titleInput = GuiFactory.createInput("Title", true, titleDefault);
        ui.configure(titleInput);

        String descriptionDefault = task != null ? task.getDescription() : "";
        InputView descriptionInput = GuiFactory.createInput("Description", false, descriptionDefault);
        ui.configure(descriptionInput);
        
        ButtonView acceptButton = GuiFactory.createDialogButton(task == null ? "Accept" : "Save", Color.STEELBLUE, false);
        ui.configure(acceptButton);
        ButtonView cancelButton = GuiFactory.createDialogButton("Cancel", Color.STEELBLUE, false);
        ui.configure(cancelButton);
        
        GridView content = TaskDialog.createContent(titleInput, descriptionInput);
        TaskDialog dialog = new TaskDialog(content, acceptButton, cancelButton);

        acceptButton.setAction(() -> {
            if (task == null) {
                String title = dialog.getInput("title").getInputText();
                String description = dialog.getInput("description").getInputText();
                taskService.saveTask(new Task(title, description));
            } else {
                task.setTitle(dialog.getInput("title").getInputText());
                task.setDescription(dialog.getInput("description").getInputText());
                taskService.saveTask(task);
            }
            TaskGui.quitTerminal(ui);

        });
        cancelButton.setAction(() -> {
            ui.setModal(null);
            TaskGui.quitTerminal(ui);
        });
        // ui.configure(content);
        dialog.registerInput("title", titleInput);
        dialog.registerInput("description", descriptionInput);
        dialog.registerInput("accept", acceptButton);
        dialog.registerInput("cancel", cancelButton);
        ui.configure(dialog);
        return dialog;
    }
}
