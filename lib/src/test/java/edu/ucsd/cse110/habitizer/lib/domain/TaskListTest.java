package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TaskListTest {
    public final static List<Task> simpleTasks = List.of(
            new SimpleTask(0, "Morning Task 1"),
            new SimpleTask(1, "Morning Task 2"),
            new SimpleTask(2, "Morning Task 3")
    );
    public List<Task> tasks = List.of(
            new OriginalTask(0, "Task 0",true),
        new OriginalTask(1, "Task 1",true),
        new OriginalTask(2, "Task 2",true),
        new OriginalTask(3, "Task 3",true)
    );


    @Test
    public void testForceSet() {
        var task1 = new OriginalTask(1, "Task1",true);
        task1.setCheckedOff(true, 0);
        assertTrue(task1.getCheckedOffStatus());
        task1.setCheckedOff(false, 0);
        assertTrue(task1.getCheckedOffStatus());
    }

    @Test
    public void testEditTaskName(){
        tasks = TaskList.editTaskName(tasks,0,"new task name");
        assertEquals("new task name", tasks.get(0).getTaskName());
        assertEquals("Task 1", tasks.get(1).getTaskName());
        assertEquals("Task 2", tasks.get(2).getTaskName());
        assertEquals("Task 3", tasks.get(3).getTaskName());
        assertEquals(4, tasks.size());

    }

    @Test
    public void testAddTask() {
        List<Task> updatedTasks = TaskList.addTask(tasks,new OriginalTask(0, "Task 4",true));
        updatedTasks = TaskList.addTask(updatedTasks,new OriginalTask(6, "Task 5",true));
        updatedTasks = TaskList.addTask(updatedTasks,new OriginalTask(null, "Task 6",true));

        assertEquals(7,updatedTasks.size());
        assertEquals("Task 4", updatedTasks.get(4).getTaskName());
        assertEquals("Task 5", updatedTasks.get(5).getTaskName());
        assertEquals("Task 6", updatedTasks.get(6).getTaskName());
    }

    @Test
    public void testRemoveTask() {
        List<Task> updatedTasks = TaskList.removeTask(tasks, 2);

        assertEquals(3, updatedTasks.size());

        // Check that task with ID 2 is gone
        assertFalse(updatedTasks.stream().anyMatch(task -> task.getTaskName().equals("Task 2")));

        // Check that IDs are sequential
        assertEquals(0,(int) updatedTasks.get(0).getId());
        assertEquals(1, (int) updatedTasks.get(1).getId());
        assertEquals(2, (int) updatedTasks.get(2).getId());

        assertEquals("Task 0", updatedTasks.get(0).getTaskName());
        assertEquals("Task 1", updatedTasks.get(1).getTaskName());
        assertEquals("Task 3", updatedTasks.get(2).getTaskName());

        updatedTasks = TaskList.removeTask(updatedTasks, 1);
        updatedTasks = TaskList.removeTask(updatedTasks, 0);

        assertEquals(1, updatedTasks.size());
        assertEquals("Task 3", updatedTasks.get(0).getTaskName());


    }
}
