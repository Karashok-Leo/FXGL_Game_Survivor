package dev.csu.survivor.achievements;

public class UserAchieveVO
{
    private final String name;
    private final String description;
    private final String imagePath;
    private final String unlockTime;

    public UserAchieveVO(String name, String description, String imagePath, String unlockTime)
    {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.unlockTime = unlockTime;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public String getUnlockTime()
    {
        return unlockTime;
    }
}
