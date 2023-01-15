package dev.fg.dhbw.ase.tasktracker.domain.entities;

import java.util.UUID;

import dev.fg.dhbw.ase.tasktracker.domain.vo.EMail;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Password;

class User
{
    private final UUID id;
    private EMail mail;
    private Password password;

    public User(EMail mail, Password password)
    {
        this.id = UUID.randomUUID();
        this.mail = mail;
        this.password = password;
    }

    public UUID getId()
    {
        return this.id;
    }

    public EMail getEMail()
    {
        return this.mail;
    }

    public Password getPassword()
    {
        return this.password;
    }
}
