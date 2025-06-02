package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
public class TaskTest {

    // Test for getter
    @Test
    public void testGetter(){
        var task1 = new OriginalTask(1, "Task1",true,1);
        assertEquals(Integer.valueOf(1), task1.getId());
        assertEquals("Task1", task1.getName());
        assertEquals(false, task1.getCheckedOffStatus());
        assertTrue(task1.isMorningTask());
    }

    // Test for setter
    @Test
    public void testSetter(){
        var task1 = new OriginalTask(1, "Task1",true,1);
        task1.setCheckedOff(true, "0 min");
        task1.setCheckedOff(true, "0 min");
        task1.setName("Task2");
        assertTrue(task1.getCheckedOffStatus());
        assertEquals("Task2", task1.getName());
    }

    @Test
    public void testSetCheckoff() {
        var task1 = new OriginalTask(1, "Task1",true,1);
        task1.setCheckedOff(true, "0 min");
        task1.setCheckedOff(true, "0 min");
        //if it is already checked off then nothing should change
        task1.setCheckedOff(false, "100 min");
        assertTrue(task1.getCheckedOffStatus());
        assertEquals("0 min", task1.getCheckedOffTime());
    }

    @Test
    public void testGetCheckOffTime() {
        var task = new OriginalTask(1, "Task1", true,1);
        task.setCheckedOff(true, "100 min");
        assertEquals("100 min", task.getCheckedOffTime());
    }
}
