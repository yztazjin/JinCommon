package ttyy.com.common.text.selection;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import ttyy.com.common.text.selection.base.UiSeMenu;
import ttyy.com.common.text.selection.base.UiSelector;

/**
 * Author: hjq
 * Date  : 2018/09/09 17:21
 * Name  : UiSeMenuDefault
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class UiSeMenuDefault implements UiSeMenu {

    UiSelector mUiSelector;

    int mMenuWidth;
    int mMenuHeight;

    int mOffsetY;

    PopupWindow mMenuPopup;

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

        mMenuPopup = new PopupWindow(mUiSelector.getContext());
        mMenuPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mMenuPopup.setClippingEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMenuPopup.setElevation(0.8f);
        }

        View mMenuContentView = getMenuContentView();
        mMenuPopup.setContentView(mMenuContentView);
        mMenuPopup.setHeight(mMenuHeight);
        mMenuPopup.setWidth(mMenuWidth);
    }

    private View getMenuContentView(){
        Context context = getUiSelector().getContext();
        float density = mUiSelector.getContext().getResources().getDisplayMetrics().density;

        int unitWidth = (int) (density * 45);
        int unitHeight = (int) (density * 30);

        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.HORIZONTAL);
        GradientDrawable radiusDrawable = new GradientDrawable();
        radiusDrawable.setCornerRadius(density * 3);
        radiusDrawable.setColor(Color.parseColor("#bb000000"));
        root.setBackground(radiusDrawable);
        root.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        TextView copy = new TextView(context);
        copy.setText("复制");
        copy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        copy.setTextColor(Color.WHITE);
        copy.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp_copy = new LinearLayout.LayoutParams(unitWidth, unitHeight);
        root.addView(copy, lp_copy);

        TextView search = new TextView(context);
        search.setText("搜索");
        search.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        search.setTextColor(Color.WHITE);
        search.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp_search = new LinearLayout.LayoutParams(unitWidth, unitHeight);
        root.addView(search, lp_search);

        mMenuWidth = unitWidth * 2;
        mMenuHeight = unitHeight;
        mOffsetY = (int) (density * 1.5);

        return root;
    }

    @Override
    public int getMenuWidth() {
        return mMenuWidth;
    }

    @Override
    public int getMenuHeight() {
        return mMenuHeight;
    }

    @Override
    public void showAtRawPoint(View view, float x, float y) {
        x -= getMenuWidth() / 2;
        y = y - getMenuHeight() - mOffsetY;
        mMenuPopup.showAtLocation(view, Gravity.NO_GRAVITY, (int)x, (int)y);
    }

    @Override
    public void updateRawPoint(float x, float y) {
        x -= getMenuWidth() / 2;
        y = y - getMenuHeight() - mOffsetY;
        mMenuPopup.update((int)x, (int)y, -1, -1);
    }

    @Override
    public void updateSize(int width, int height) {
        mMenuPopup.update(width, height);
    }

    @Override
    public boolean isShowing() {
        return mMenuPopup.isShowing();
    }

    @Override
    public void dismiss() {
        mMenuPopup.dismiss();
    }

}
