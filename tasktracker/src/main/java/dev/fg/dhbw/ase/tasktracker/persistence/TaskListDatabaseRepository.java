package dev.fg.dhbw.ase.tasktracker.persistence;

import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.query.Query;

import dev.fg.dhbw.ase.tasktracker.domain.entities.Task;
import dev.fg.dhbw.ase.tasktracker.domain.entities.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.entities.User;
import dev.fg.dhbw.ase.tasktracker.domain.factories.TaskFactory;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Title;

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
        Query<TaskList> query = session.createQuery("FROM TaskList WHERE userId = :user", TaskList.class);
        query.setParameter("user", user);
        return query.getResultList();
    }

    @Override
    public void createNewTaskList(Title name)
    {
        session.save(new TaskList(name));
    }

    @Override
    public void deleteTaskList(TaskList taskList)
    {
        session.beginTransaction();
        session.delete(taskList);
        session.getTransaction().commit();
    }

    @Override
    public void updateTaskList(UUID taskListId, Title name)
    {
        TaskList taskList = session.find(TaskList.class, taskListId);
        taskList.changeTitle(name);
        session.merge(taskList);
    }

    @Override
    public void createNewTaskListForUser(Title name, User user)
    {
        session.beginTransaction();
        TaskList taskList = new TaskList(user.getId(), name);
        session.save(taskList);
        session.getTransaction().commit();
    }

    @Override
    public List<Task> getTasksOfTaskListByTaskListName(Title name)
    {
        session.beginTransaction();
        Query<TaskList> list = session.createQuery("FROM TaskList WHERE title = :name", TaskList.class);
        list.setParameter("name", name);
        UUID id = list.uniqueResult().getId();
        Query<Task> getTasksOfListQuery = session.createQuery("FROM Task WHERE taskListId = :id", Task.class);
        getTasksOfListQuery.setParameter("id", id);
        List<Task> tasks = getTasksOfListQuery.getResultList();
        session.getTransaction().commit();
        return tasks;
    }

    @Override
    public void addTaskToTaskList(Task t)
    {
        session.beginTransaction();
        session.save(t);
        session.getTransaction().commit();
    }

    @Override
    public TaskList getTaskListByName(Title name)
    {
        session.beginTransaction();
        Query<TaskList> query = session.createQuery("FROM TaskList WHERE title = :name", TaskList.class);
        query.setParameter("name", name);
        TaskList list = query.uniqueResult();
        session.getTransaction().commit();
        return list;
    }

    @Override
    public void removeTask(Task task)
    {
        session.beginTransaction();
        session.remove(task);
        session.getTransaction().commit();
    }

    @Override
    public void moveTaskToList(Task task, TaskList list) // TODO: wrong name or not needed
    {
        session.beginTransaction();
        session.delete(task);
        Task newTask1 = TaskFactory.createTaskDone(task, list.getId());
        Task newTask2 = TaskFactory.createTaskDone(task, task.getTaskListId());
        session.save(newTask1);
        session.save(newTask2);
        session.getTransaction().commit();
    }

    @Override
    public List<Task> getTasksDoneForUser(UUID user)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void markTaskAsDone(Task task)
    {
        session.beginTransaction();
        session.delete(task);
        Task taskDone = TaskFactory.createTaskDone(task, task.getTaskListId());
        session.save(taskDone);
        session.getTransaction().commit();
    }

    @Override
    public void markTaskAsUndone(Task task)
    {
        session.beginTransaction();
        session.delete(task);
        Task taskUndone = TaskFactory.createTaskUndone(task);
        session.save(taskUndone);
        session.getTransaction().commit();
    }
}
