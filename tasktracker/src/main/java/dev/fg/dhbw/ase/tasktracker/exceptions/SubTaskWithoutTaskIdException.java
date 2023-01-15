package dev.fg.dhbw.ase.tasktracker.exceptions;

public class SubTaskWithoutTaskIdException extends RuntimeException
{
    public SubTaskWithoutTaskIdException()
    {
        super("A sub task must have a superior task.");
    }
}