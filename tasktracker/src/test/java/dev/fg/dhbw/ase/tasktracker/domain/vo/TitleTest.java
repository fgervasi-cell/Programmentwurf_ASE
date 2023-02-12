package dev.fg.dhbw.ase.tasktracker.domain.vo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import dev.fg.dhbw.ase.tasktracker.exceptions.InvalidTitleException;

public class TitleTest
{
    @Test(expected = InvalidTitleException.class)
    public void testEmptyTitle()
    {
        new Title("");
    }

    @Test(expected = InvalidTitleException.class)
    public void testStringIsNull()
    {
        new Title(null);
    }

    @Test(expected = InvalidTitleException.class)
    public void testOnlyBlanks()
    {
        new Title("        ");
    }

    @Test
    public void testWorkingExample()
    {
        Title t = new Title("This should work");
        assertEquals("This should work", t.getTitleString());
    }
    
    @Test
    public void testEquals()
    {
        Title title1 = new Title("Title 1");
        Title title2 = new Title("Title 1");
        Title title3 = new Title("Title 3");
        assertEquals(title1, title2);
        assertNotEquals(title2, title3);
    }

    @Test
    public void testHashCode()
    {
        Title title1 = new Title("Title 1");
        Title title2 = new Title("Title 1");
        Title title3 = new Title("Title 3");
        assertEquals(title1.hashCode(), title2.hashCode());
        assertNotEquals(title2.hashCode(), title3.hashCode());
    }
}
