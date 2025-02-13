package edu.ucsd.cse110.habitizer.lib.domain;


import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;

public class MorningEveningTasklistTest {
    InMemoryDataSource dataSource = new InMemoryDataSource();
    TaskRepository taskRepository = new TaskRepository(dataSource);

    public final static List<Task> tasks = List.of(
            new Task(0, "Morning Task 1",true),
            new Task(1, "Morning Task 2",true),
            new Task(2, "Morning Task 3",true),
            new Task(3, "Evening Task 1",false),
            new Task(4, "Evening Task 2",false),
            new Task(5, "Evening Task 3",false),
            new Task(null, "Evening Task 4",false),
            new Task(null, "Evening Task 5",false)
    );

    @Test
    public void testSaveAndRemoveTask(){
        for(Task task : tasks){
            taskRepository.save(task);
        }
        var all = taskRepository.findAll();
        assertEquals((int) taskRepository.count(), 8);

        taskRepository.remove(1);

        assertEquals((int) taskRepository.count(), 7);

    }

}
