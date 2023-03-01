package dev.fg.dhbw.ase.tasktracker.abstraction.observer;

import java.util.ArrayList;
import java.util.List;

public class Observable
{
    List<Observer> observers = new ArrayList<>();

    public void registerObserver(final Observer observer)
    {
        this.observers.add(observer);
    }

    public void unregisterObserver(Observer observer)
    {
        this.observers.remove(observer);
    }

    public void notifyObservers(Object event)
    {
        for (Observer observer : this.observers)
        {
            observer.notifyObserver(event);
        }
    }
}
