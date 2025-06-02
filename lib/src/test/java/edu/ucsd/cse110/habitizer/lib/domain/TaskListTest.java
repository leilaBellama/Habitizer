package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TaskListTest {
    // Test to force set, expected to not work
    public final static List<Task> simpleTasks = List.of(
            new SimpleTask(0, "Morning Task 1"),
            new SimpleTask(1, "Morning Task 2"),
            new SimpleTask(2, "Morning Task 3"),
            new SimpleTaskBuilder().setCheckedOff(true).buildSimpleTask()
    );
    public List<Task> tasks = List.of(
            new OriginalTask(0, "Task 0",true,1),
        new OriginalTask(1, "Task 1",true,2),
        new OriginalTask(2, "Task 2",true,3),
        new OriginalTask(3, "Task 3",true,4)
    );

    @Test
    public void testEditTaskName() {
        List<Task> originalList = new ArrayList<>();
        originalList.add(new OriginalTask(1, "Task One",true,1));
        originalList.add(new OriginalTask(2, "Task Two",true,2));

        List<Task> result = TaskList.editTaskName(originalList, 1, "Updated Task One");

        assertEquals("Updated Task One", result.get(0).getName());  // Task 1 updated
        assertEquals("Task Two", result.get(1).getName());  // Task 2 remains unchanged
    }

    @Test
    public void testAddTask_TaskAddedSuccessfully() {
        List<Task> originalList = new ArrayList<>();
        originalList.add(new OriginalTask(1, "Task One",true,1));

        Task newTask = new OriginalTask(2, "Task Two",true,2);
        List<Task> result = TaskList.addTask(originalList, newTask);

        assertEquals(2, result.size()); // Ensure new task is added
        assertEquals("Task Two", result.get(1).getName()); // Check new task name
    }


    @Test
    public void testResetAll() {
        var list = TaskList.resetAll(simpleTasks);
        var v = list.get(3).getCheckedOffStatus();
        for (int i = 0; i < list.size(); i++) {
            assertFalse(list.get(i).getCheckedOffStatus());
            assertNull(list.get(i).getCheckedOffTime());
        }
    }

    @Test
    public void testForceSet() {
        var task1 = new OriginalTask(1, "Task1",true,1);
        task1.setCheckedOff(true, "0");
        assertTrue(task1.getCheckedOffStatus());
        task1.setCheckedOff(false, "0");
        assertTrue(task1.getCheckedOffStatus());
    }


}
