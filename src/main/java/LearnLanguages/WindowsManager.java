package LearnLanguages;

public class WindowsManager
{
    private boolean ifWordIsBeingEnteredFromMenu;
    private boolean ifWordIsBeingModified;
    private boolean ifWordIsBeingBrowsed;

    public WindowsManager(Boolean ifWordIsBeingEnteredFromMenu, Boolean ifWordIsBeingModified,
                          Boolean ifWordIsBeingBrowsed)
    {
        this.ifWordIsBeingEnteredFromMenu = ifWordIsBeingEnteredFromMenu;
        this.ifWordIsBeingModified = ifWordIsBeingModified;
        this.ifWordIsBeingBrowsed = ifWordIsBeingBrowsed;
    }

    public void setIfWordIsBeingEnteredFromMenu(Boolean newValue)
    {
        ifWordIsBeingEnteredFromMenu = newValue;
    }

    public void setIfWordIsdBeingModified(Boolean newValue)
    {
        ifWordIsBeingModified = newValue;
    }

    public void setIfsWordIsdBeingBrowsed(Boolean newValue)
    {
        ifWordIsBeingBrowsed = newValue;
    }

    public boolean getIfWordIsBeingEnteredFromMenu()
    {
        return ifWordIsBeingEnteredFromMenu;
    }

    public boolean getIfWordIsBeingModified()
    {
        return ifWordIsBeingModified;
    }

    public boolean getIfWordIsBeingBrowsed()
    {
        return ifWordIsBeingBrowsed;
    }
}
