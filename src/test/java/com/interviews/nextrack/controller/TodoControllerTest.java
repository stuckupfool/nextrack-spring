package com.interviews.nextrack.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.interviews.nextrack.service.TaskService;
import com.interviews.nextrack.todo.Task;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController todoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
    }

    @Test
    void getAllTodos_returnsListOfTasks() throws Exception {
        Task task1 = new Task("Task 1", "Desc 1", false, 1);
        task1.setId(1L);
        Task task2 = new Task("Task 2", "Desc 2", true, 2);
        task2.setId(2L);
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Task 1")))
                .andExpect(jsonPath("$[0].description", is("Desc 1")))
                .andExpect(jsonPath("$[0].completed", is(false)))
                .andExpect(jsonPath("$[0].rank", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Task 2")))
                .andExpect(jsonPath("$[1].completed", is(true)));

        verify(taskService).getAllTasks();
    }

    @Test
    void getAllTodos_returnsEmptyListWhenNoTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(taskService).getAllTasks();
    }

    @Test
    void getTodoById_existingId_returnsTask() throws Exception {
        Task task = new Task("Buy milk", "2% organic milk", false, 1);
        task.setId(1L);

        when(taskService.getTask(1L)).thenReturn(task);

        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Buy milk")))
                .andExpect(jsonPath("$.description", is("2% organic milk")))
                .andExpect(jsonPath("$.completed", is(false)))
                .andExpect(jsonPath("$.rank", is(1)));

        verify(taskService).getTask(1L);
    }

    @Test
    void getTodoById_nonExistingId_returnsNotFound() throws Exception {
        when(taskService.getTask(99L)).thenReturn(null);

        mockMvc.perform(get("/api/todos/99"))
                .andExpect(status().isNotFound());

        verify(taskService).getTask(99L);
    }

    @Test
    void createTodo_validTask_returnsCreatedWithLocationAndTask() throws Exception {
        Task savedTask = new Task("New Task", "Description", false, 0);
        savedTask.setId(10L);

        when(taskService.saveTask(any(Task.class))).thenReturn(savedTask);

        String taskJson = """
                {
                    "title": "New Task",
                    "description": "Description",
                    "completed": false,
                    "rank": 0
                }
                """;

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/todos/10"))
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.title", is("New Task")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.completed", is(false)));

        verify(taskService).saveTask(any(Task.class));
    }

    @Test
    void updateTodo_existingId_returnsUpdatedTask() throws Exception {
        Task existingTask = new Task("Old Title", "Old Desc", false, 1);
        existingTask.setId(1L);

        Task updatedTask = new Task("Updated Title", "Updated Desc", true, 3);
        updatedTask.setId(1L);

        when(taskService.getTask(1L)).thenReturn(existingTask);
        when(taskService.saveTask(any(Task.class))).thenReturn(updatedTask);

        String updateJson = """
                {
                    "title": "Updated Title",
                    "description": "Updated Desc",
                    "completed": true,
                    "rank": 3
                }
                """;

        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.description", is("Updated Desc")))
                .andExpect(jsonPath("$.completed", is(true)))
                .andExpect(jsonPath("$.rank", is(3)));

        verify(taskService).getTask(1L);
        verify(taskService).saveTask(existingTask);
    }

    @Test
    void updateTodo_nonExistingId_returnsNotFound() throws Exception {
        when(taskService.getTask(99L)).thenReturn(null);

        String updateJson = """
                {
                    "title": "Updated Title",
                    "description": "Updated Desc"
                }
                """;

        mockMvc.perform(put("/api/todos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound());

        verify(taskService).getTask(99L);
        verify(taskService, never()).saveTask(any(Task.class));
    }

    @Test
    void toggleTodoCompletion_existingId_togglesAndReturnsUpdatedTask() throws Exception {
        Task initialTask = new Task("Task to toggle", "Desc", false, 0);
        initialTask.setId(1L);

        Task toggledTask = new Task("Task to toggle", "Desc", true, 0);
        toggledTask.setId(1L);

        when(taskService.getTask(1L))
                .thenReturn(initialTask)
                .thenReturn(toggledTask);

        mockMvc.perform(patch("/api/todos/1/toggle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.completed", is(true)));

        verify(taskService).toggleTaskCompletion(1L);
    }

    @Test
    void toggleTodoCompletion_nonExistingId_returnsNotFound() throws Exception {
        when(taskService.getTask(99L)).thenReturn(null);

        mockMvc.perform(patch("/api/todos/99/toggle"))
                .andExpect(status().isNotFound());

        verify(taskService).getTask(99L);
        verify(taskService, never()).toggleTaskCompletion(99L);
    }

    @Test
    void deleteTodo_existingId_returnsNoContent() throws Exception {
        Task existingTask = new Task("Task to delete", "Desc", false, 0);
        existingTask.setId(1L);

        when(taskService.getTask(1L)).thenReturn(existingTask);

        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());

        verify(taskService).getTask(1L);
        verify(taskService).deleteTask(1L);
    }

    @Test
    void deleteTodo_nonExistingId_returnsNotFound() throws Exception {
        when(taskService.getTask(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/todos/99"))
                .andExpect(status().isNotFound());

        verify(taskService).getTask(99L);
        verify(taskService, never()).deleteTask(99L);
    }
}