package com.interviews.nextrack.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.interviews.nextrack.repository.TaskRepository;
import com.interviews.nextrack.todo.Task;

@Component
@Order(1)
public class ExampleTodos implements CommandLineRunner {
    @Autowired
    private TaskRepository repository;

    private static final Logger log = LoggerFactory.getLogger(ExampleTodos.class);

    public ExampleTodos() {
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding " + repository.save(new Task("Buy groceries", "Milk, Eggs, Bread", false, 1)));
        log.info("Seeding " + repository.save(new Task("Clean the house", "Living room, Kitchen, Bathroom", true, 2)));
        log.info("Seeding " + repository.save(new Task("Finish homework", "Math, Science, History", false, 3)));
        log.info("Seeding " + repository.save(new Task("Exercise", "Run 5km, Stretching", false, 4)));
        log.info("Seeding " + repository.save(new Task("Read a book", "Fiction or Non-fiction", true, 5)));
        log.info("Seeding " + repository.save(new Task("Learn a new skill", "Programming, Cooking, Music", true, 6)));
        log.info("Seeding " + repository.save(new Task("Plan a trip", "Destination, Budget, Itinerary", false, 7)));
        log.info("Seeding " + repository.save(new Task("Meditate", "Morning and Evening", false, 8)));
        log.info("Seeding " + repository.save(new Task("Call a friend", "Catch up and chat", true, 9)));
        log.info("Seeding " + repository.save(new Task("Write a journal", "Reflect on the day", false, 10)));
    }
}