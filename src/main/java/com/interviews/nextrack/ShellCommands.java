package com.interviews.nextrack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.stereotype.Component;

import com.interviews.nextrack.gui.TaskGui;

@Component
public class ShellCommands {
    @Autowired
    private TaskGui taskGui;

    @Command(name = "add", description = "Adds a new todo item to the task list")
    public void add() {
        taskGui.add(null);
    }

    @Command(name = "edit", description = "Edits an existing todo item")
    public void edit() {
        taskGui.find();
    }

    @Command(name = "list", description = "Lists all todo items")
    public void list() {
        taskGui.list(null);
    }

    @Command(name = "toggle", description = "Marks a todo item as completed and resets a completed item back to todo")
    public void toggle() {
        taskGui.toggle(null);
    }

    @Command(name = "delete", description = "Deletes a todo item from the task list")
    public void delete() {
        taskGui.delete(null);
    }
}
