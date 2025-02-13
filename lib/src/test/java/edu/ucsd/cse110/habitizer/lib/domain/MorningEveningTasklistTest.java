package edu.ucsd.cse110.habitizer.lib.domain;


import org.junit.Test;

import static org.junit.Assert.*;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;

public class MorningEveningTasklistTest {
    InMemoryDataSource dataSource = new InMemoryDataSource();
    TaskRepository taskRepository = new TaskRepository(dataSource);

    @Test
    public void testSaveAndRemoveTask(){
        var task1 = new Task(1,"new task morning",true);
        taskRepository.save(task1);
        var numTaskAfter = taskRepository.count();

        assertEquals((int) numTaskAfter, 1);
        taskRepository.remove(1);

        var numTaskAfterRemove = taskRepository.count();

        assertEquals((int) numTaskAfterRemove, 0);
    }

}
