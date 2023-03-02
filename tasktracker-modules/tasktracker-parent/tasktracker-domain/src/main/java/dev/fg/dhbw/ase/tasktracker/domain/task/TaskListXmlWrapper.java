package dev.fg.dhbw.ase.tasktracker.domain.task;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "task-lists")
public class TaskListXmlWrapper
{
    @XmlElement(name = "task-list")
    private List<TaskList> lists;

    @SuppressWarnings("unused")
    private TaskListXmlWrapper()
    {

    }

    public TaskListXmlWrapper(List<TaskList> lists)
    {
        this.lists = lists;
    }

    public List<TaskList> getTaskLists()
    {
        return this.lists;
    }
}
