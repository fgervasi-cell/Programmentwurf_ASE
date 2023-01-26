package dev.fg.dhbw.ase.tasktracker.persistence;

import java.util.List;
import java.util.UUID;

import dev.fg.dhbw.ase.tasktracker.domain.entities.Task;
import dev.fg.dhbw.ase.tasktracker.domain.entities.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.entities.User;
import dev.fg.dhbw.ase.tasktracker.domain.vo.TaskTitle;

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
    public void createNewTaskList(TaskTitle name)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteTaskList(TaskList taskList)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateTaskList(UUID taskListId, TaskTitle name)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void createNewTaskListForUser(TaskTitle name, User user)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Task> getTasksOfTaskListByTaskListName(TaskTitle name)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addTaskToTaskList(Task t, TaskTitle name)
    {
        // TODO Auto-generated method stub
        
    }
}
