package dev.csu.survivor.ui;

import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.Constants;
import dev.csu.survivor.achievements.UserAchieveVO;
import dev.csu.survivor.util.StyleUtil;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class AchievementPane extends VBox implements IPaginationBox
{
    protected final GridPane gridPane;
    protected final List<BorderStackPane> entries;
    private final SimpleIntegerProperty currentPage = new SimpleIntegerProperty(0);

    public AchievementPane(Collection<UserAchieveVO> achieveList, int achievedCount, int totalAchievements)
    {
        super();
        this.entries = achieveList.stream().map(vo -> new BorderStackPane(300, 400, createAchievementBox(vo))).toList();

        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        this.setPage(0);

        VBox achievementBox = this.createWholeBox(Constants.Client.INVENTORY_ENTRY_SPACING);

        StackPane progressPane = createProgressPane(achievedCount, totalAchievements);

        achievementBox.getChildren().addFirst(progressPane);
        achievementBox.setTranslateX(-225);
        achievementBox.setTranslateY(-200);

        this.getChildren().addAll(achievementBox);
    }

    protected static StackPane createProgressPane(int achievedCount, int totalAchievements)
    {
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(925);

        VBox progressContainer = new VBox(10);
        progressContainer.setAlignment(Pos.CENTER);
        BorderPane borderPane = getBorderPane(progressContainer);

        double progress = (double) achievedCount / totalAchievements;
        progressBar.setProgress(progress);

        Label progressLabel = new Label("Achieved  " + achievedCount + "  out of " + totalAchievements + "  achievements  (" + (int) (progress * 100) + "%)");

        StyleUtil.setLabelStyle(progressLabel, 18);

        progressContainer.getChildren().addAll(progressLabel, progressBar);

        return new StackPane(borderPane);
    }

    private static @NotNull BorderPane getBorderPane(VBox progressContainer)
    {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(progressContainer);
        borderPane.setStyle(
                "-fx-border-color: white; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 10; " +
                "-fx-background-color: rgba(0, 0, 0, 0.2);"
        );
        return borderPane;
    }

    // 创建单个成就的显示框，包含成就图片、名称、描述和达成时间
    private static VBox createAchievementBox(UserAchieveVO achieve)
    {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        // 成就图片
        ImageView imageView = new ImageView(FXGL.image("achievements/" + achieve.getImagePath()));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        // 成就名称
        Label nameLabel = new Label(achieve.getName());
        StyleUtil.setLabelStyle(nameLabel, 24);

        // 成就描述
        Label descriptionLabel = new Label(achieve.getDescription());
        StyleUtil.setLabelStyle(descriptionLabel, 18);
        descriptionLabel.setMaxWidth(270);
        descriptionLabel.setWrapText(true);

        // 达成时间
        Label timeLabel = new Label("Unlock Time: \n" + achieve.getUnlockTime());
        StyleUtil.setLabelStyle(timeLabel, 16);

        // 空白标签用于分隔上下内容
        Label spacer1 = new Label();
        Label spacer2 = new Label();
        spacer1.setMinHeight(50);
        spacer2.setMinHeight(30);

        box.getChildren().addAll(imageView, nameLabel, spacer1, descriptionLabel, spacer2, timeLabel);

        return box;
    }

    @Override
    public IntegerProperty currentPageProperty()
    {
        return currentPage;
    }

    @Nullable
    @Override
    public Node createEmptyEntry()
    {
//        return new BorderStackPane(300, 400);
        return null;
    }

    @Override
    public GridPane getEntryPane()
    {
        return gridPane;
    }

    @Override
    public List<? extends Node> getEntries()
    {
        return entries;
    }

    @Override
    public int getColumns()
    {
        return 3;
    }

    @Override
    public int getRows()
    {
        return 1;
    }
}
