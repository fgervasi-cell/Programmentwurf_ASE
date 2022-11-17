package dev.fg.dhbw.ase.tasktracker.domain;

public final class TaskTitle 
{
    private final String title;
    
    public TaskTitle(final String title)
    {
        if (title == null || title.isEmpty() || title.isBlank())
        {
            throw new InvalidTitleException();
        }
        this.title = title;
    }

    public String getTitle()
    {
        return this.title;
    }
}
