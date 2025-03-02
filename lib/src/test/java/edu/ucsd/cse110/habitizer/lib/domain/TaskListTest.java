package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;

public class TaskListTest {
    public InMemoryDataSource testSource = new InMemoryDataSource();
    public TaskRepository testRepository = new TaskRepository(testSource);

    @Test
    public void testAddTask(){
        var task1 = new Task(1, "Task1",true);
        var task2 = new Task(2, "Task2",false);
        List<Task> expectedList = List.of(
                task1,
                task2
        );
        //save() is called in addTask()
        testRepository.save(task1);
        testRepository.save(task2);

        List<Task> testList = testSource.getTasks();
        assertEquals(testList, expectedList);
    }
}
