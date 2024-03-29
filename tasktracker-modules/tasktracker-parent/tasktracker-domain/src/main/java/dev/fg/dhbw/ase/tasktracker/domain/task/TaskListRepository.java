package dev.fg.dhbw.ase.tasktracker.domain.task;

import java.util.List;
import java.util.UUID;

import dev.fg.dhbw.ase.tasktracker.domain.user.User;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Title;

public interface TaskListRepository 
{
    public TaskList getTaskList(final UUID taskListId);
    public TaskList getTaskListByName(final Title name);
    public List<TaskList> getTaskListsForUser(final UUID user);
    public void createNewTaskList(final Title name);
    public void createNewTaskListForUser(final Title name, final User user);
    public void deleteTaskList(final TaskList taskList);
    public void updateTaskList(final UUID taskListId, final Title name);
    public List<Task> getTasksOfTaskListByTaskListName(Title name);
    public void addTaskToTaskList(final Task t);
    public void removeTask(final Task task);
    public List<Task> getTasksDoneForUser(final UUID user);
    public void markTaskAsDone(final Task task);
    public void markTaskAsUndone(final Task task);
    public int getNumberOfDoneTasksForUser(final User user);
    public int getNumberOfOpenTasksForUser(final User user);
    public List<Task> getDoneTasksOfLastWeek(final User user);
}
