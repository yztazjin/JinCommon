package ttyy.com.common.text.selection.base;

import android.view.View;

/**
 * Author: hjq
 * Date  : 2018/09/08 15:36
 * Name  : UiSeCursor
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface UiSeCursor {

    UiSelector getUiSelector();

    void setUiSelector(UiSelector selector);

    void setCursorType(CursorType type);

    CursorType getCursorType();

    int getCursorWidth();

    int getCursorHeight();

    void showAtRawPoint(View view, float x, float y);

    void updateRawPoint(float x, float y);

    void updateSize(int width, int height);

    boolean isShowing();

    void dismiss();

}
