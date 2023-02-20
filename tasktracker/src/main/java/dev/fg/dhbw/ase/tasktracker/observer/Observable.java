package dev.fg.dhbw.ase.tasktracker.observer;

public interface Observable
{
    // TODO: maybe make this an abstract class because those implementations are always the same
    public void registerObserver(final Observer observer);
    public void unregisterObserver(Observer observer);
    public void notifyObservers(Object event);
}
