package com.interviews.nextrack.gui;

import java.util.List;

import org.springframework.shell.jline.tui.component.message.ShellMessageBuilder;
import org.springframework.shell.jline.tui.component.view.TerminalUI;
import org.springframework.shell.jline.tui.component.view.control.ListView;
import org.springframework.shell.jline.tui.component.view.control.ListView.ItemStyle;
import org.springframework.shell.jline.tui.component.view.control.MenuBarView;
import org.springframework.shell.jline.tui.component.view.control.MenuBarView.MenuBarItem;
import org.springframework.shell.jline.tui.component.view.control.MenuView.MenuItem;
import org.springframework.shell.jline.tui.component.view.control.MenuView.MenuItemCheckStyle;
import org.springframework.shell.jline.tui.component.view.control.StatusBarView;
import org.springframework.shell.jline.tui.component.view.control.View;
import org.springframework.shell.jline.tui.component.view.event.EventLoop;
import org.springframework.shell.jline.tui.component.view.event.KeyEvent.Key;

import com.interviews.nextrack.service.TaskService;
import com.interviews.nextrack.todo.Task;

public class TaskListView extends NexTrackAppView {

    public TaskListView(View main, View menuBar, View statusBar, TerminalUI ui) {
        super(main, menuBar, statusBar, ui);
    }

    public static TaskListView construct(TerminalUI ui, TaskService taskService) {
        MenuBarView menuBar = getMenuBarView(ui);
        ListView<Task> mainView = TaskListView.getTaskListView(taskService);
        StatusBarView statusBar = TaskListView.getStatusBarView(taskService);
        TaskListView view = new TaskListView(mainView, menuBar, statusBar, ui);
        return view;
    }
    
    public static MenuBarView getMenuBarView(TerminalUI ui) {
        EventLoop eventLoop = ui.getEventLoop();
        Runnable quitAction = () -> {
            eventLoop.dispatch(ShellMessageBuilder.ofInterrupt());
        };
        Runnable aboutAction = () -> {
            AboutDialog aboutView = AboutDialog.construct(ui);
            if (aboutView != null) {
                ui.setModal(aboutView);
                ui.setFocus(aboutView);
            }
        };
        MenuBarView menuBar = MenuBarView.of(
                MenuBarItem.of(
                        "File",
                        MenuItem.of("Quit", MenuItemCheckStyle.NOCHECK, quitAction))
                        .setHotKey(Key.f),
                MenuBarItem.of(
                        "Help",
                        MenuItem.of("About", MenuItemCheckStyle.NOCHECK, aboutAction))
                        .setHotKey(Key.h));
        ui.configure(menuBar);
        return menuBar;
    }

    public static ListView<Task> getTaskListView(TaskService taskService) {
        ListView<Task> view = new ListView<>(ItemStyle.NOCHECK);
        view.setCellFactory((list, item) -> new TaskCell(item));
        List<Task> tasks = taskService.getAllTasks();
        view.setItems(tasks);
        view.setShowBorder(true);
        view.setTitle("Tasks (All)");
        return view;
    }

    public static  StatusBarView getStatusBarView(TaskService taskService) {
        long taskCount = taskService.getTaskCount();
        long completedCount = taskService.getCompletedTaskCount();
        StatusBarView statusBar = new StatusBarView(List.of(
                new StatusBarView.StatusItem("Tasks: " + taskCount),
                new StatusBarView.StatusItem("Completed: " + completedCount),
                new StatusBarView.StatusItem("Pending: " + (taskCount - completedCount))));
        // (new StatusItem[] { new StatusItem.of("Total Tasks: " + taskCount) });
        return statusBar;
    }
}
