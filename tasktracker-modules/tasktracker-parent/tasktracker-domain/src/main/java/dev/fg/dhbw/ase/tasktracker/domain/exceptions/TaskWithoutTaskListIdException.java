package dev.fg.dhbw.ase.tasktracker.domain.exceptions;

public class TaskWithoutTaskListIdException extends RuntimeException
{
    public TaskWithoutTaskListIdException()
    {
        super("A task must always be contained inside a task list.");
    }
}
