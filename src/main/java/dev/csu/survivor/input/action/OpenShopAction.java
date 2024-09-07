package dev.csu.survivor.input.action;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import dev.csu.survivor.ui.menu.ShopMenu;

public class OpenShopAction extends UserAction
{
    public OpenShopAction()
    {
        super("Open Shop");
    }

    @Override
    protected void onActionBegin()
    {
        FXGL.getSceneService().pushSubScene(new ShopMenu());
    }
}
