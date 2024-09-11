package dev.csu.survivor.ui.menu;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;

/**
 * 游戏菜单界面（按下 ESC 暂停时显示）
 */
public class GameMenu extends BaseMenu
{
    public GameMenu()
    {
        super(MenuType.GAME_MENU);
    }

    @Override
    protected void initMenuBox(MenuBox menuBox)
    {
//        var enabledItems = FXGL.getSettings().getEnabledMenuItems();

        menuBox.add(createResumeButton());

//        if (enabledItems.contains(MenuItem.SAVE_LOAD))
//        {
//            var itemSave = new MenuButton("menu.save");
//            itemSave.setOnAction(e -> fireSave());
//
//            var itemLoad = new MenuButton("menu.load");
//            itemLoad.setMenuContent(createContentLoad(), false);
//
//            menuBox.add(itemSave);
//            menuBox.add(itemLoad);
//        }

        var itemOptions = new MenuButton("menu.options");
        itemOptions.setChild(createOptionsMenu());
        menuBox.add(itemOptions);

//        if (enabledItems.contains(MenuItem.EXTRA))
//        {
//            var itemExtra = new MenuButton("menu.extra");
//            itemExtra.setChild(createExtraMenu());
//            menuBox.add(itemExtra);
//        }

        if (FXGL.getSettings().isMainMenuEnabled())
        {
            var itemExit = new MenuButton("menu.mainMenu");
            itemExit.setOnAction(e -> fireExitToMainMenu());
            menuBox.add(itemExit);
        } else
        {
            var itemExit = new MenuButton("menu.exit");
            itemExit.setOnAction(e -> fireExit());
            menuBox.add(itemExit);
        }
    }
}
