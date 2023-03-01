package dev.fg.dhbw.ase.tasktracker.plugins.components;

public enum ComponentEvent
{
    TASK_LIST_DELETE, TASK_LIST_NAME_CLICKED, TASK_DELETE, TASK_DONE, ADD_TASK_FORM_SAVE;

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
