package dev.fg.dhbw.ase.tasktracker.domain.vo;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.persistence.Embeddable;

import dev.fg.dhbw.ase.tasktracker.domain.exceptions.DateLiesInPastException;

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
        if (!this.isFutureDate(date))
        {
            throw new DateLiesInPastException();
        }
        this.date = date;
    }

    private boolean isFutureDate(Date date)
    {
        Calendar today = Calendar.getInstance(TimeZone.getDefault());
        Calendar future = Calendar.getInstance(TimeZone.getDefault());
        future.setTime(date);

        return today.get(Calendar.MONTH) <= future.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) <= future.get(Calendar.DAY_OF_MONTH)
                && today.get(Calendar.YEAR) <= future.get(Calendar.YEAR);
    }

    public Date getDueDate()
    {
        return this.date;
    }

    public DateInFuture changeDate(Date newDate)
    {
        return new DateInFuture(newDate);
    }

    public String formatDate()
    {
        String dateString = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.getDueDate());
        dateString = String.format("%s-%s-%s", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR));
        return dateString;
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