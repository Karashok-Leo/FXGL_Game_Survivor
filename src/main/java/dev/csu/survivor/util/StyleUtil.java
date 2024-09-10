package dev.csu.survivor.util;

import com.almasb.fxgl.dsl.FXGL;
import dev.csu.survivor.Constants;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class StyleUtil
{
    public static void setLabelStyle(Label label)
    {
        setLabelStyle(label, Constants.Style.FONT_SIZE, Constants.Style.FONT_COLOR, Constants.Style.FONT_OTHER_STYLE);
    }

    public static void setLabelStyle(Label label, Color color)
    {
        setLabelStyle(label, Constants.Style.FONT_SIZE, color, Constants.Style.FONT_OTHER_STYLE);
    }

    public static void setLabelStyle(Label label, double size)
    {
        setLabelStyle(label, size, Constants.Style.FONT_COLOR, Constants.Style.FONT_OTHER_STYLE);
    }

    public static void setLabelStyle(Label label, double size, Color color)
    {
        setLabelStyle(label, size, color, Constants.Style.FONT_OTHER_STYLE);
    }

    public static void setLabelStyle(Label label, double size, Color color, String otherStyle)
    {
        label.setFont(FXGL.getUIFactoryService().newFont(size));
        label.setTextFill(color);
        label.setStyle(otherStyle);
    }

    public static void setTextStyle(Text text)
    {
        setTextStyle(text, Constants.Style.FONT_SIZE, Constants.Style.FONT_COLOR);
    }

    public static void setTextStyle(Text text, double size)
    {
        setTextStyle(text, size, Constants.Style.FONT_COLOR);
    }

    public static void setTextStyle(Text text, Paint paint)
    {
        setTextStyle(text, Constants.Style.FONT_SIZE, paint);
    }

    public static void setTextStyle(Text text, double size, Paint paint)
    {
        text.setFill(paint);
        text.setFont(FXGL.getUIFactoryService().newFont(size));

        DropShadow shadow = new DropShadow(Constants.Style.SHADOW_GAUSSIAN_RADIUS, Color.BLACK);
        shadow.setInput(new Glow(Constants.Style.SHADOW_GLOW_RADIUS));
        text.setEffect(shadow);
    }
}
