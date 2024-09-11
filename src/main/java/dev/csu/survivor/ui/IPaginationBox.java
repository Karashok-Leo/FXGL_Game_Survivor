package dev.csu.survivor.ui;

import dev.csu.survivor.ui.menu.ShopMenu;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 便于创建翻页界面的接口
 */
public interface IPaginationBox
{
    IntegerProperty currentPageProperty();

    /**
     * 设置当前页码
     *
     * @param page 页码
     */
    default void setPage(int page)
    {
        this.currentPageProperty().set(Math.clamp(
                page,
                0,
                this.getEntries().isEmpty() ?
                        0 :
                        (this.getEntries().size() - 1) / this.getEntriesPerPage()
        ));
        this.updatePage();
    }

    /**
     * @return 空项目
     */
    @Nullable
    Node createEmptyEntry();

    /**
     * @return 排列项目的 GridPane
     */
    GridPane getEntryPane();

    /**
     * @return 所有项目的列表
     */
    List<? extends Node> getEntries();

    /**
     * @return 每页的列数
     */
    int getColumns();

    /**
     * @return 每页的行数
     */
    int getRows();

    /**
     * @return 每页的项目数量
     */
    default int getEntriesPerPage()
    {
        return this.getColumns() * this.getRows();
    }

    /**
     * 创建翻页按钮
     *
     * @return 翻页按钮的 HBox 布局
     */
    default HBox createButtonBox()
    {
        BorderStackPane previous = ShopMenu.createBorderButton(
                "Previous",
                200,
                button -> this.prev()
        );
        BorderStackPane next = ShopMenu.createBorderButton(
                "Next",
                200,
                button -> this.next()
        );
        HBox buttonBox = new HBox(previous, next);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(80);
        return buttonBox;
    }

    /**
     * 创建整个 VBox 布局
     *
     * @param spacing 项目和翻页按钮之间的间隔
     * @return 项目和翻页按钮的 VBox 布局
     */
    default VBox createWholeBox(double spacing)
    {
        VBox wholeBox = new VBox(getEntryPane(), this.createButtonBox());
        wholeBox.setSpacing(spacing);
        return wholeBox;
    }

    /**
     * 更新当前页
     */
    default void updatePage()
    {
        this.getEntryPane().getChildren().clear();
        int size = this.getEntries().size();
        int startIndex = currentPageProperty().get() * this.getEntriesPerPage();
        for (int i = 0; i < this.getEntriesPerPage(); i++)
        {
            int index = startIndex + i;
            int column = i % this.getColumns();
            int row = i / this.getColumns();
            Node node = index < size ? this.getEntries().get(index) : createEmptyEntry();
            if (node != null)
                this.getEntryPane().add(node, column, row);
        }
    }

    /**
     * 上一页
     */
    default void prev()
    {
        this.setPage(currentPageProperty().get() - 1);
    }

    /**
     * 下一页
     */
    default void next()
    {
        this.setPage(currentPageProperty().get() + 1);
    }
}
