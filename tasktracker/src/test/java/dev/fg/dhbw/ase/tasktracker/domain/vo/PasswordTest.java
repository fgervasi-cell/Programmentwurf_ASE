package dev.fg.dhbw.ase.tasktracker.domain.vo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import dev.fg.dhbw.ase.tasktracker.exceptions.InvalidPasswordException;

public class PasswordTest
{
    @Test(expected = InvalidPasswordException.class)
    public void testTooFewCharacters()
    {
        new Password("TooFew!");
    }

    @Test(expected = InvalidPasswordException.class)
    public void testInvalidCharacters()
    {
        new Password("@");
    }

    @Test
    public void testWorkingExample()
    {
        Password pass = new Password("ValidPassword0123456789");
        assertEquals("ValidPassword0123456789", pass.getPassword());
    }

    @Test
    public void testEquals()
    {
        Password pass1 = new Password("Password1");
        Password pass2 = new Password("Password1");
        Password pass3 = new Password("Password3");
        assertEquals(pass1, pass2);
        assertNotEquals(pass2, pass3);
    }

    @Test
    public void testHashCode()
    {
        Password pass1 = new Password("Password1");
        Password pass2 = new Password("Password1");
        Password pass3 = new Password("Password3");
        assertEquals(pass1.hashCode(), pass2.hashCode());
        assertNotEquals(pass2.hashCode(), pass3.hashCode());
    }
}
