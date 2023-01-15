package dev.fg.dhbw.ase.tasktracker.persistence;

import java.util.List;
import java.util.UUID;

import dev.fg.dhbw.ase.tasktracker.domain.entities.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.vo.TaskTitle;

public interface TaskListRepository 
{
    public TaskList getTaskList(final UUID taskListId);
    public List<TaskList> getTaskListsForUser(final UUID user);
    public void createNewTaskList(final TaskTitle name);
    public void deleteTaskList(final TaskList taskList);
    public void updateTaskList(final UUID taskListId, final TaskTitle name);
}
