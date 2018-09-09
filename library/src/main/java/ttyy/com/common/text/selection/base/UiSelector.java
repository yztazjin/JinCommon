package ttyy.com.common.text.selection.base;

import android.content.Context;

import ttyy.com.common.text.selection.SelectionInfo;

/**
 * Author: hjq
 * Date  : 2018/09/08 14:15
 * Name  : Selector
 * Intro : Edit By hjq
 * Version : 1.0
 */
public interface UiSelector {

    Context getContext();

    SelectionInfo getSelectionInfo();

    void setUiSelection(int start, int end);

    void updateUiSelectionOffsetFromWinPoint(CursorType type, float winX, float winY);

    void updateUiSelectionOffsetFromRawPoint(CursorType type, float rawX, float rawY);

    void updateUiSelectionOffsetFromRelPoint(CursorType type, float relX, float relY);

    UiSeResources getUiResources();

    float[] getRelPointForSelectionOffset(int offset);

    float[] getRawPointForSelectionOffset(int offset);

    float[] getWinPointForSelectionOffset(int offset);

    int[] getWinPointForRealView();

    int[] getRawPointForRealView();

    void showUiCursors();

    void dismissUiCursors();

    void showUiMenu();

    void dismissUiMenu();

    void destroy();

}
