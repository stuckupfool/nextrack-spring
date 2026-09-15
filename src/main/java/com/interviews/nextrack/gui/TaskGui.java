package com.interviews.nextrack.gui;

import com.interviews.nextrack.service.TaskService;
import com.interviews.nextrack.todo.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.jline.tui.component.message.ShellMessageBuilder;
import org.springframework.shell.jline.tui.component.view.control.View;
import org.springframework.shell.jline.tui.component.view.event.EventLoop;
import org.springframework.shell.jline.tui.component.view.TerminalUI;
import org.springframework.shell.jline.tui.component.view.TerminalUIBuilder;
import org.springframework.stereotype.Component;

@FunctionalInterface
interface ViewFactory {
    View create(TerminalUI ui, TaskService taskService);
}

@Component
public class TaskGui {
    @Autowired
    private TerminalUIBuilder builder;

    @Autowired
    private TaskService taskService;

    private void ViewWrapper(TerminalUI ui, ViewFactory viewFactory) {
        final TerminalUI resolvedUi = (ui == null) ? builder.build() : ui;
        View view = viewFactory.create(resolvedUi, taskService);

        resolvedUi.setRoot(view, true);
        resolvedUi.setFocus(view);
        if (ui == null) {
            resolvedUi.run();
        }
    }

    public void list(TerminalUI ui) {
        ViewWrapper(ui, (uiFactory, taskService) -> TaskListView.construct(uiFactory, taskService));
    }

    public void add(TerminalUI ui) {
        ViewWrapper(ui, (uiFactory, taskService) -> TaskDialog.construct(uiFactory, taskService));
    }

    public void toggle(TerminalUI ui) {
        ViewWrapper(ui, (uiFactory, taskService) -> FindDialog.construct((task) -> {
            taskService.toggleTaskCompletion(task.getId());
            TaskGui.quitTerminal(uiFactory);
        }, uiFactory, taskService, "Please enter the ID of the todo task to complete/uncomplete."));
    }

    public void delete(TerminalUI ui) {
        ViewWrapper(ui, (uiFactory, taskService) -> FindDialog.construct((task) -> {
            if (task == null) {
                ErrorDialog errorDialog = ErrorDialog.construct(uiFactory, "Could not find todo task");
                uiFactory.setRoot(errorDialog, true);
                uiFactory.setFocus(errorDialog);
                return;
            }
            taskService.deleteTask(task.getId());
            TaskGui.quitTerminal(uiFactory);
        }, uiFactory, taskService, "Please enter the ID of the todo task to delete."));
    }

    public void find() {
        TerminalUI ui = builder.build();

        FindDialog findDialog = FindDialog.construct((task) -> edit(task, ui), ui, taskService);

        ui.setRoot(findDialog, true);
        ui.setFocus(findDialog);
        ui.run();
    }

    public void edit(Task task, TerminalUI ui) {
        // // TerminalUI ui = builder.build();x

        if (task == null) {
            ErrorDialog errorDialog = ErrorDialog.construct(ui, "Could not find todo task");
            ui.setRoot(errorDialog, true);
            ui.setFocus(errorDialog);
            return;
        }
        ViewWrapper(ui, (uiFactory, taskService) -> TaskDialog.construct(task, uiFactory, taskService));
    }

    public static void quitTerminal(TerminalUI ui) {
        // Quit
        EventLoop eventLoop = ui.getEventLoop();
        eventLoop.dispatch(ShellMessageBuilder.ofInterrupt());
    }
}