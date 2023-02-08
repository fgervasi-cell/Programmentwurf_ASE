package dev.fg.dhbw.ase.tasktracker.persistence;

import java.util.List;
import java.util.UUID;

import dev.fg.dhbw.ase.tasktracker.domain.entities.Task;
import dev.fg.dhbw.ase.tasktracker.domain.entities.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.entities.User;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Title;

class TaskListFileSystemRepository implements TaskListRepository
{
    @Override
    public TaskList getTaskList(UUID taskListId)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TaskList> getTaskListsForUser(UUID user)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createNewTaskList(Title name)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteTaskList(TaskList taskList)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateTaskList(UUID taskListId, Title name)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void createNewTaskListForUser(Title name, User user)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Task> getTasksOfTaskListByTaskListName(Title name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addTaskToTaskList(Task t)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public TaskList getTaskListByName(Title name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeTask(Task task)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void moveTaskToList(Task task, TaskList list)
    {
        // TODO Auto-generated method stub
        
    }
}
