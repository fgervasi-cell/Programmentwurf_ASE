package dev.fg.dhbw.ase.tasktracker.domain.vo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import dev.fg.dhbw.ase.tasktracker.domain.exceptions.InvalidEMailException;

public class EMailTest
{
    @Test(expected = InvalidEMailException.class)
    public void testMissingAtSymbol()
    {
        new EMail("example.com");
    }

    @Test(expected = InvalidEMailException.class)
    public void testMissingDomain()
    {
        new EMail("user@");
    }

    @Test(expected = InvalidEMailException.class)
    public void testMissingUser()    
    {
        new EMail("@host.com");
    }

    @Test(expected = InvalidEMailException.class)
    public void testMultipleAtSymbols()
    {
        new EMail("user@domain.com@");
    }

    @Test
    public void testEquals()
    {
        EMail mail1 = new EMail("example@host.com");
        EMail mail2 = new EMail("example@host.com");
        EMail mail3 = new EMail("user@domain.de");
        EMail mail4 = new EMail("Example@Host.com");
        assertEquals(mail1, mail2);
        assertNotEquals(mail2, mail3);
        assertEquals(mail2, mail4);
    }

    @Test
    public void testHashCode()
    {
        EMail mail1 = new EMail("example@host.com");
        EMail mail2 = new EMail("example@host.com");
        EMail mail3 = new EMail("user@domain.de");
        EMail mail4 = new EMail("Example@Host.com");
        assertEquals(mail1.hashCode(), mail2.hashCode());
        assertNotEquals(mail2.hashCode(), mail3.hashCode());
        assertEquals(mail2.hashCode(), mail4.hashCode());
    }
}
