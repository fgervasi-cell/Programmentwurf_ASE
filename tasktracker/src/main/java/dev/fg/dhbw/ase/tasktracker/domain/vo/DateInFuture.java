package dev.fg.dhbw.ase.tasktracker.domain.vo;

import java.util.Date;

import javax.persistence.Embeddable;

import dev.fg.dhbw.ase.tasktracker.exceptions.DateLiesInPastException;

@Embeddable
public final class DateInFuture
{
    private final Date date;

    @SuppressWarnings("unused")
    private DateInFuture()
    {
        this.date = null;
    }

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
            return this.date.equals(((DateInFuture) obj).date);
        }
        return false;
    }
}