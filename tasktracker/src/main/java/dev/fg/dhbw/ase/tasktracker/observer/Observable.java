package dev.fg.dhbw.ase.tasktracker.observer;

public interface Observable
{
    public void registerObserver(final Observer observer);
    public void notifyObservers(Object event);
}
