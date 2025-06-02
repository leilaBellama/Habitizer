package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SimpleTaskBuilderTest {

    private SimpleTaskBuilder builder;

    @Before
    public void setUp() {
        builder = new SimpleTaskBuilder();
    }

    @Test
    public void testSimpleTaskBuilder() {
        Integer expectedId = 1;
        String expectedTaskName = "Task 1";
        Boolean expectedCheckedOff = true;
        String expectedCheckedOffTime = "10 mins";
        Integer expectedRoutineId = 101;

        SimpleTask task = builder.setId(expectedId)
                .setTaskName(expectedTaskName)
                .setCheckedOff(expectedCheckedOff)
                .setCheckedOffTime(expectedCheckedOffTime)
                .setRoutineId(expectedRoutineId)
                .buildSimpleTask();

        // Verify the task's properties using assertions
        assertNotNull(task);
        assertEquals(expectedId, task.getId());
        assertEquals(expectedTaskName, task.getName());
        assertTrue(task.getCheckedOffStatus());
        assertEquals(expectedCheckedOffTime, task.getCheckedOffTime());
        assertEquals(expectedRoutineId, task.getRoutineId());
    }

    @Test
    public void testTaskNameSetter() {
        String taskName = "Test Task";
        SimpleTask task = builder.setTaskName(taskName).buildSimpleTask();
        assertEquals(taskName, task.getName());
    }

    @Test
    public void testCheckedOffSetter() {
        boolean checkedOff = true;
        SimpleTask task = builder.setCheckedOff(checkedOff).buildSimpleTask();
        assertTrue(task.getCheckedOffStatus());
    }

    @Test
    public void testCheckedOffTimeSetter() {
        String checkedOffTime = "2 mins";
        SimpleTask task = builder.setCheckedOffTime(checkedOffTime).buildSimpleTask();
        assertEquals(checkedOffTime, task.getCheckedOffTime());
    }

    @Test
    public void testRoutineIdSetter() {
        Integer routineId = 123;
        SimpleTask task = builder.setRoutineId(routineId).buildSimpleTask();
        assertEquals(routineId, task.getRoutineId());
    }

    @Test
    public void testIdSetter() {
        Integer id = 1;
        SimpleTask task = builder.setId(id).buildSimpleTask();
        assertEquals(id, task.getId());
    }
}
