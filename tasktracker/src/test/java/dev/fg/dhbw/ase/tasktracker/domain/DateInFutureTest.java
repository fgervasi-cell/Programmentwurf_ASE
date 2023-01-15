package dev.fg.dhbw.ase.tasktracker.domain;

import static org.junit.Assert.assertEquals;
import java.util.Date;

import org.junit.Test;

import dev.fg.dhbw.ase.tasktracker.domain.vo.DateInFuture;
import dev.fg.dhbw.ase.tasktracker.exceptions.DateLiesInPastException;

public class DateInFutureTest 
{
    @Test(expected = DateLiesInPastException.class)
    public void testInvalidInstantiation()
    {
        Date date = new Date(System.currentTimeMillis());
        new DateInFuture(date);
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
