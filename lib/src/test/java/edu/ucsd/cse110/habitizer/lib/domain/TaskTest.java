package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
public class TaskTest {
    // Test for getter
    @Test
    public void testGetter(){
        var task1 = new Task(1, "Task1");
        assertEquals(Integer.valueOf(1), task1.getId());
        assertEquals("Task1", task1.getTaskName());
        assertEquals(false, task1.getCheckedOffStatus());
    }

    // Test for setter
    @Test
    public void testSetter(){
        var task1 = new Task(1, "Task1");
        task1.setCheckedOff(true, 0);
        task1.setName("Task2");
        assertEquals(true, task1.getCheckedOffStatus());
        assertEquals("Task2", task1.getTaskName());
    }
}
