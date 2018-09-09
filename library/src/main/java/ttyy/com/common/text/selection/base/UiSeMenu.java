package ttyy.com.common.text.selection.base;

import android.view.View;

/**
 * Author: hjq
 * Date  : 2018/09/09 17:20
 * Name  : UiSeMenu
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface UiSeMenu {

    UiSelector getUiSelector();

    void setUiSelector(UiSelector selector);

    int getMenuWidth();

    int getMenuHeight();

    void showAtWinPoint(View view, float x, float y, MenuGravity direction);

    void updateWinPoint(float x, float y, MenuGravity direction);

    void updateSize(int width, int height);

    boolean isShowing();

    void dismiss();

}
