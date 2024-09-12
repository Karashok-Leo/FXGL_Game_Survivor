package dev.csu.survivor.enums;

import dev.csu.survivor.user.User;

import java.util.function.BooleanSupplier;

public enum AchievementType
{
    EMPTY(() -> false),
    FIRST_LOGIN(() -> User.getInstance().getLastLoginDate() == null),
    FIRST_GAME(() -> true),
    ;
    private final BooleanSupplier condition;

    AchievementType(BooleanSupplier condition)
    {
        this.condition = condition;
    }

    public boolean checkCondition()
    {
        return condition.getAsBoolean();
    }
}
