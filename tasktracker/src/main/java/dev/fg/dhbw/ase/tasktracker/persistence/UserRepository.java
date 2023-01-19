package dev.fg.dhbw.ase.tasktracker.persistence;

import java.util.UUID;

import dev.fg.dhbw.ase.tasktracker.domain.entities.User;
import dev.fg.dhbw.ase.tasktracker.domain.vo.EMail;

public interface UserRepository 
{
    public User getUserById(final UUID user);
    public User getUserByEMail(final EMail mail);
    public void createUser(final User user); 
}
