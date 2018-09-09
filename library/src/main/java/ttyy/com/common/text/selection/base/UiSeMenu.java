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

    void showAtRawPoint(View view, float x, float y);

    void updateRawPoint(float x, float y);

    void updateSize(int width, int height);

    boolean isShowing();

    void dismiss();

}
