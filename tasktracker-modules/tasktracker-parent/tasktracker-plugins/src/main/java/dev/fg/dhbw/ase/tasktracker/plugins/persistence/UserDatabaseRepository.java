package dev.fg.dhbw.ase.tasktracker.plugins.persistence;

import java.util.UUID;


import org.hibernate.Session;
import org.hibernate.query.Query;

import dev.fg.dhbw.ase.tasktracker.domain.user.User;
import dev.fg.dhbw.ase.tasktracker.domain.user.UserRepository;
import dev.fg.dhbw.ase.tasktracker.domain.vo.EMail;

public class UserDatabaseRepository implements UserRepository
{
    private final Session session;

    public UserDatabaseRepository(final Session session)
    {
        this.session = session;
    }

    @Override
    public User getUserById(UUID user)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User getUserByEMail(EMail mail)
    {
        Query<User> query = session.createQuery("FROM User WHERE mail = :mail", User.class);
        query.setParameter("mail", mail);
        return query.uniqueResult();
    }

    @Override
    public void createUser(User user)
    {
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }
}
