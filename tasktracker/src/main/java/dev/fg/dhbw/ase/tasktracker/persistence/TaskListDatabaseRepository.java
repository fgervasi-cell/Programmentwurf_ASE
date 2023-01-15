package dev.fg.dhbw.ase.tasktracker.persistence;

import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.query.Query;

import dev.fg.dhbw.ase.tasktracker.domain.entities.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.vo.TaskTitle;

class TaskListDatabaseRepository implements TaskListRepository
{
    private final Session session;

    public TaskListDatabaseRepository(final Session session)
    {
        this.session = session;
    }

    @Override
    public TaskList getTaskList(UUID taskListId)
    {
        return session.find(TaskList.class, taskListId);
    }

    @Override
    public List<TaskList> getTaskListsForUser(UUID user)
    {
        Query<TaskList> query = session.createQuery("FROM TaskList WHERE userID = :user", TaskList.class);
        query.setParameter("user", user);
        return query.getResultList();
    }

    @Override
    public void createNewTaskList(TaskTitle name)
    {
        session.save(new TaskList(name));
    }

    @Override
    public void deleteTaskList(TaskList taskList)
    {
        session.delete(taskList);
    }

    @Override
    public void updateTaskList(UUID taskListId, TaskTitle name)
    {
        TaskList taskList = session.find(TaskList.class, taskListId);
        taskList.changeTitle(name);
        session.merge(taskList);
    }
}
