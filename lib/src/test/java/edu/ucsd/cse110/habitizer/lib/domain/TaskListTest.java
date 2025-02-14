package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TaskListTest {
    // Test to force set, expected to not working
    @Test
    public void testForceSet() {
        var task1 = new Task(1, "Task1");
        task1.setCheckedOff(true, 0);
        assertTrue(task1.getCheckedOffStatus());
        task1.setCheckedOff(false, 0);
        assertTrue(task1.getCheckedOffStatus());
    }

    // Test for reset function
    @Test
    public void testReset() {
        var task1 = new Task(1, "Task1");
        task1.setCheckedOff(true, 0);
        assertTrue(task1.getCheckedOffStatus());
        task1.reset();
        assertFalse(task1.getCheckedOffStatus());
    }

    // Test for setting checkedOffTime
    @Test
    public void testSetCheckedOffTime() {
        var task1 = new Task(1, "Task1");
        task1.setCheckedOff(true, 0);
        assertTrue(task1.getCheckedOffStatus());
        assertEquals(0, (int)task1.getCheckedOffTime());
    }
}
