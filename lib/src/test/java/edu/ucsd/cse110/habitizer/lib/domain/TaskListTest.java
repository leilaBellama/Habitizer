package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TaskListTest {
    // Test to force set, expected to not work
    @Test
    public void testForceSet() {
        var task1 = new Task(1, "Task1",true);
        task1.setCheckedOff(true, 0);
        assertTrue(task1.getCheckedOffStatus());
        task1.setCheckedOff(false, 0);
        assertTrue(task1.getCheckedOffStatus());
    }

}
