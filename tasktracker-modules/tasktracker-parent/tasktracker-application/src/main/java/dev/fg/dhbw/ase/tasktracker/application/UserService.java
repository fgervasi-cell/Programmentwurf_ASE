package dev.fg.dhbw.ase.tasktracker.application;

import dev.fg.dhbw.ase.tasktracker.domain.user.User;
import dev.fg.dhbw.ase.tasktracker.domain.user.UserRepository;
import dev.fg.dhbw.ase.tasktracker.domain.vo.EMail;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Password;

public class UserService
{
    private UserRepository repository;

    public UserService(UserRepository repository)
    {
        this.repository = repository;
    }

    public boolean userPasswordDoesMatch(Password actual, Password input)
    {
        return actual.equals(input);
    }

    public void registerUser(User user)
    {
        repository.createUser(user);
    }

    public User getUserByEMail(EMail mail)
    {
        return repository.getUserByEMail(mail);
    }
}
