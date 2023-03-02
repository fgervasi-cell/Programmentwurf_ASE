package dev.fg.dhbw.ase.tasktracker.domain.task;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tasks")
public class TaskXmlWrapper 
{
    @XmlElement(name = "task")
    private List<Task> tasks;

    @SuppressWarnings("unused")
    private TaskXmlWrapper()
    {
        
    }

    public TaskXmlWrapper(List<Task> tasks)
    {
        this.tasks = tasks;
    }

    public List<Task> getTasks()
    {
        return this.tasks;
    }
}
