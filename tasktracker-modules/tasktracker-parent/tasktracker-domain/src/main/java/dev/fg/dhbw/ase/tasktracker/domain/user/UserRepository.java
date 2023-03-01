package dev.fg.dhbw.ase.tasktracker.domain.user;

import dev.fg.dhbw.ase.tasktracker.domain.vo.EMail;

public interface UserRepository 
{
    public User getUserByEMail(final EMail mail);
    public void createUser(final User user); 
}
