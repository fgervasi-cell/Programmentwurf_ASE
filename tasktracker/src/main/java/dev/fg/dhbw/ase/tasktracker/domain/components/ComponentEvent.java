package dev.fg.dhbw.ase.tasktracker.domain.components;

public enum ComponentEvent
{
    TASK_LIST_DELETE, TASK_LIST_NAME_CLICKED, TASK_DELETE, TASK_DONE;

    private String data;

    public String getData()
    {
        return this.data;
    }

    protected void setData(String data)
    {
        this.data = data;
    }
}
