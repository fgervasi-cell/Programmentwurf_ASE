package dev.fg.dhbw.ase.tasktracker.domain;

import java.util.Date;

public final class DateInFuture
{
    private final Date date;

    public DateInFuture(final Date date)
    {
        if (date.getTime() < System.currentTimeMillis())
        {
            throw new DateLiesInPastException();
        }
        this.date = date;
    }

    public Date getDueDate()
    {
        return this.date;
    }

    public DateInFuture changeDate(Date newDate)
    {
        return new DateInFuture(newDate);
    }

    @Override
    public int hashCode() 
    {
        return this.date.hashCode();
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (obj instanceof DateInFuture)
        {
            return this.date.equals(((DateInFuture)obj).date);
        }
        return false;
    }
}