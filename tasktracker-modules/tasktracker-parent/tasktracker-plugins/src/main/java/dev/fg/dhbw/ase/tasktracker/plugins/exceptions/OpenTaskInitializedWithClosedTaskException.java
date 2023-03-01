package dev.fg.dhbw.ase.tasktracker.plugins.exceptions;

public class OpenTaskInitializedWithClosedTaskException extends RuntimeException
{
    public OpenTaskInitializedWithClosedTaskException()
    {
        super("You cannot initialize an object of type 'OpenTaskComponent' with a closed task.");
    }
}
