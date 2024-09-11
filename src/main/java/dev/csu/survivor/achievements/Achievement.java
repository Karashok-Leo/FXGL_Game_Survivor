package dev.csu.survivor.achievements;

public class Achievement
{
    private final int id;
    private final String name;
    private final String description;
    private final String imagePath; // 只保存图片名称

    public Achievement(int id, String name, String description, String imageName)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imagePath = imageName;
    }

    public int getId()
    {
        return id;
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
}


