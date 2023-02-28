package dev.fg.dhbw.ase.tasktracker.domain.vo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import dev.fg.dhbw.ase.tasktracker.domain.exceptions.DateLiesInPastException;

public class DateInFutureTest 
{
    @Test(expected = DateLiesInPastException.class)
    public void testInvalidInstantiation()
    {
        Calendar date = Calendar.getInstance(TimeZone.getDefault());
        date.set(1970, 12, 29);
        new DateInFuture(date.getTime());
    }

    @Test
    public void testTaskDueToday()
    {
        Date today = Calendar.getInstance(TimeZone.getDefault()).getTime();
        DateInFuture future = new DateInFuture(today);
        assertNotNull(future.getDueDate());
    }

    @Test
    public void testEquals()
    {
        long currentTimeMillis = System.currentTimeMillis();
        DateInFuture date1 = new DateInFuture(new Date(currentTimeMillis));
        DateInFuture date2 = new DateInFuture(new Date(currentTimeMillis));
        assertEquals(date1, date2);
    }

    @Test
    public void testHashCode()
    {
        long currentTimeMillis = System.currentTimeMillis();
        DateInFuture date1 = new DateInFuture(new Date(currentTimeMillis));
        DateInFuture date2 = new DateInFuture(new Date(currentTimeMillis));
        assertEquals(date1.hashCode(), date2.hashCode());
    }
}
