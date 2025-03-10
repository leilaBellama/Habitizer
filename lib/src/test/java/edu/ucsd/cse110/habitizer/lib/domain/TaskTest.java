package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
public class TaskTest {



    // Test for getter
    @Test
    public void testGetter(){
        var task1 = new OriginalTask(1, "Task1",true);
        assertEquals(Integer.valueOf(1), task1.getId());
        assertEquals("Task1", task1.getName());
        assertEquals(false, task1.getCheckedOffStatus());
        assertTrue(task1.isMorningTask());
    }

    // Test for setter
    @Test
    public void testSetter(){
        var task1 = new OriginalTask(1, "Task1",true);
        task1.setCheckedOff(true, 0);
        task1.setName("Task2");
        assertTrue(task1.getCheckedOffStatus());
        assertEquals("Task2", task1.getName());
    }

    @Test
    public void testSetCheckoff() {
        var task1 = new OriginalTask(1, "Task1",true);
        task1.setCheckedOff(true, 0);
        //if it is already checked off then nothing should change
        task1.setCheckedOff(false, 100);
        assertTrue(task1.getCheckedOffStatus());
        assertEquals(Integer.valueOf(0), task1.getCheckedOffTime());
    }

    /*
    @Test
    public void testReset() {
        var task = new OriginalTask(1, "Task1", true);
        task.setCheckedOff(true, 0);
        task.reset();
        assertFalse(task.getCheckedOffStatus());
    }

     */

    @Test
    public void testGetCheckOffTime() {
        var task = new OriginalTask(1, "Task1", true);
        task.setCheckedOff(true, 100);
        assertEquals(Integer.valueOf(100), task.getCheckedOffTime());
    }
}
