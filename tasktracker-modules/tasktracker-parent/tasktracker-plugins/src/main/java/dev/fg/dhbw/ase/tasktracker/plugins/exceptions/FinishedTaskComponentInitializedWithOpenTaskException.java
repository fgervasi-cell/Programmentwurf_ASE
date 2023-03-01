package dev.fg.dhbw.ase.tasktracker.plugins.exceptions;

public class FinishedTaskComponentInitializedWithOpenTaskException extends RuntimeException
{
    public FinishedTaskComponentInitializedWithOpenTaskException()
    {
        super("You cannot initialize an object of type 'FinishedTaskComponent' with an open task.");
    }
}
