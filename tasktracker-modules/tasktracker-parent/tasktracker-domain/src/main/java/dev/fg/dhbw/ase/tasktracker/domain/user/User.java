package dev.fg.dhbw.ase.tasktracker.domain.user;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import dev.fg.dhbw.ase.tasktracker.domain.vo.EMail;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Password;

@Entity
@Table(name = "user")
public class User implements Serializable
{
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private final UUID id;
    @Embedded
    private EMail mail;
    @Embedded
    private Password password;

    @SuppressWarnings("unused")
    private User()
    {
        this.id = null;
    }

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
