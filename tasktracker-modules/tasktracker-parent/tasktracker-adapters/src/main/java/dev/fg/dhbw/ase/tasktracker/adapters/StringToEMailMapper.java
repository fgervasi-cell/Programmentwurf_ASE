package dev.fg.dhbw.ase.tasktracker.adapters;

import java.util.function.Function;

import dev.fg.dhbw.ase.tasktracker.domain.vo.EMail;

public class StringToEMailMapper implements Function<String, EMail>
{
    @Override
    public EMail apply(String s)
    {
        return map(s);
    }

    private EMail map(String s)
    {
        return new EMail(s);
    }
}
