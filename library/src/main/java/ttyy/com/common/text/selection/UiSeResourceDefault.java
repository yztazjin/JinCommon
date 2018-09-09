package ttyy.com.common.text.selection;

import android.graphics.Color;

import ttyy.com.common.text.selection.base.CursorType;
import ttyy.com.common.text.selection.base.UiSeCursor;
import ttyy.com.common.text.selection.base.UiSeMenu;
import ttyy.com.common.text.selection.base.UiSeResources;
import ttyy.com.common.text.selection.base.UiSelector;

/**
 * Author: hjq
 * Date  : 2018/09/09 09:24
 * Name  : UiSeResourceDefault
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class UiSeResourceDefault implements UiSeResources{

    UiSelector mUiSelector;
    UiSeCursor mUiSeCursorLeft;
    UiSeCursor mUiSeCursorRight;
    UiSeMenu mSeMenu;

    int mHighLightColor;

    protected UiSeResourceDefault(){
        mHighLightColor = Color.YELLOW;
    }

    @Override
    public UiSelector getUiSelector() {
        return mUiSelector;
    }

    @Override
    public void setUiSelector(UiSelector selector) {
        mUiSelector = selector;

        initialize();
    }

    private void initialize(){
        if (getUiCursor(CursorType.Left) == null){
            UiSeCursor left = new UiSeCursorDefault();
            left.setUiSelector(getUiSelector());
            left.setCursorType(CursorType.Left);
            setUiCursor(left);
        }

        if (getUiCursor(CursorType.Right) == null){
            UiSeCursor right = new UiSeCursorDefault();
            right.setUiSelector(getUiSelector());
            right.setCursorType(CursorType.Right);
            setUiCursor(right);
        }

        if (getUiMenu() == null){
            UiSeMenu menu = new UiSeMenuDefault();
            menu.setUiSelector(getUiSelector());
            setUiMenu(menu);
        }
    }

    @Override
    public UiSeCursor getUiCursor(CursorType position) {
        if (position == CursorType.Left){
            return mUiSeCursorLeft;
        }
        return mUiSeCursorRight;
    }

    @Override
    public void setUiCursor(UiSeCursor cursor) {
        if (cursor.getCursorType() == CursorType.Left){
            mUiSeCursorLeft = cursor;
        } else {
            mUiSeCursorRight = cursor;
        }
    }

    @Override
    public UiSeMenu getUiMenu() {
        return mSeMenu;
    }

    @Override
    public void setUiMenu(UiSeMenu menu) {
        mSeMenu = menu;
    }

    @Override
    public int getHighLightColor() {
        return mHighLightColor;
    }

    @Override
    public void setHighLightColor(int color) {
        mHighLightColor = color;
    }
}
