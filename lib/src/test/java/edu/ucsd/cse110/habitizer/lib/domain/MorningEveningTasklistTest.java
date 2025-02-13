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
        var id = task1.getId();

        assertEquals((int) numTaskAfter, 1);
        taskRepository.remove(1);

        var numTaskAfterRemove = taskRepository.count();

        assertEquals((int) numTaskAfterRemove, 0);
    }


/*
    @Test
    public void testFindMorning(){
        var task1 = new Task(1, "new task morning",true);
        taskRepository.saveMorning(task1);

        assertEquals(taskRepository.findMorning(1).getValue(), task1);
    }

    @Test
    public void testSaveEvening(){
        var numTaskMorning = taskRepository.countMorning();
        var numTask = taskRepository.countEvening();
        var task1 = new Task(4, "new task evening",false);
        taskRepository.saveEvening(task1);
        var numTaskAfter = taskRepository.countEvening();
        var numTaskMorningAfter = taskRepository.countMorning();

        assertEquals((int) numTaskAfter, numTask + 1);
        assertEquals((int) numTaskMorningAfter, (int) numTaskMorning);
    }

    @Test
    public void testFindEvening(){
        var task1 = new Task(1, "new task evening",false);
        taskRepository.saveEvening(task1);

        assertEquals(taskRepository.findEvening(1).getValue(), task1);
    }

 */

}
