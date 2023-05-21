package dev.fg.dhbw.ase.tasktracker.plugins.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.query.Query;

import dev.fg.dhbw.ase.tasktracker.domain.task.Task;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskListRepository;
import dev.fg.dhbw.ase.tasktracker.domain.user.User;
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
        session.merge(t);
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
    public List<Task> getTasksDoneForUser(UUID user)
    {
        List<TaskList> lists = this.getTaskListsForUser(user);
        session.beginTransaction();
        List<Task> result = new ArrayList<>();
        for (TaskList list : lists)
        {
            Query<Task> query = session.createQuery("FROM Task WHERE taskListId = :id AND done = true", Task.class);
            query.setParameter("id", list.getId());
            result.addAll(query.getResultList());
        }
        session.getTransaction().commit();
        return result;
    }

    @Override
    public void markTaskAsDone(Task task)
    {
        session.beginTransaction();
        task.markTaskAsDone();
        session.persist(task);
        session.getTransaction().commit();
    }

    @Override
    public void markTaskAsUndone(Task task)
    {
        session.beginTransaction();
        task.markTaskAsUndone();
        session.update(task);
        session.getTransaction().commit();
    }

    @Override
    public int getNumberOfDoneTasksForUser(User user)
    {
        List<Task> tasks = this.getTasksDoneForUser(user.getId());
        return tasks.size();
    }

    @Override
    public int getNumberOfOpenTasksForUser(User user)
    {
        List<TaskList> lists = this.getTaskListsForUser(user.getId());
        List<Task> result = new ArrayList<>();
        session.beginTransaction();
        for (TaskList list : lists)
        {
            Query<Task> query = session.createQuery("FROM Task WHERE taskListId = :id AND done = false", Task.class);
            query.setParameter("id", list.getId());
            result.addAll(query.getResultList());
        }
        session.getTransaction().commit();
        return result.size();
    }

    @Override
    public List<Task> getDoneTasksOfLastWeek(User user)
    {
        session.beginTransaction();
        Query<TaskList> taskLists = session.createQuery("FROM TaskList WHERE userId = :userId", TaskList.class);
        taskLists.setParameter("userId", user.getId());
        List<Task> doneTasksOfLastWeek = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_MONTH, -5);
        for (TaskList list : taskLists.getResultList())
        {
            Query<Task> tasks = session.createQuery(
                    "FROM Task WHERE taskListId = :taskListId AND done = true AND completionDate BETWEEN :startDate AND :endDate",
                    Task.class);
            tasks.setParameter("taskListId", list.getId());
            tasks.setParameter("startDate", today.getTime());
            tasks.setParameter("endDate", new Date(System.currentTimeMillis()));
            doneTasksOfLastWeek.addAll(tasks.getResultList());
        }
        session.getTransaction().commit();
        return doneTasksOfLastWeek;
    }
}
