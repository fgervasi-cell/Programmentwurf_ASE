package dev.fg.dhbw.ase.tasktracker.persistence;

import java.util.List;
import java.util.UUID;

import dev.fg.dhbw.ase.tasktracker.domain.entities.Task;
import dev.fg.dhbw.ase.tasktracker.domain.entities.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.entities.User;
import dev.fg.dhbw.ase.tasktracker.domain.vo.TaskTitle;

public interface TaskListRepository 
{
    public TaskList getTaskList(final UUID taskListId);
    public List<TaskList> getTaskListsForUser(final UUID user);
    public void createNewTaskList(final TaskTitle name);
    public void createNewTaskListForUser(final TaskTitle name, final User user);
    public void deleteTaskList(final TaskList taskList);
    public void updateTaskList(final UUID taskListId, final TaskTitle name);
    public List<Task> getTasksOfTaskListByTaskListName(TaskTitle name);
}
