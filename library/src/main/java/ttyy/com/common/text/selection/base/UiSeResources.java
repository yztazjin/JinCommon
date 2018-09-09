package ttyy.com.common.text.selection.base;

/**
 * Author: hjq
 * Date  : 2018/09/08 15:37
 * Name  : UiSeResources
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface UiSeResources {

    UiSelector getUiSelector();

    void setUiSelector(UiSelector selector);

    UiSeCursor getUiCursor(CursorType position);

    void setUiCursor(UiSeCursor cursor);

    UiSeMenu getUiMenu();

    void setUiMenu(UiSeMenu menu);

    int getHighLightColor();

    void setHighLightColor(int color);

}
