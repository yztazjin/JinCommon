package ttyy.com.common.text.selection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import ttyy.com.common.text.selection.base.CursorType;
import ttyy.com.common.text.selection.base.UiSeCursor;
import ttyy.com.common.text.selection.base.UiSelector;

/**
 * Author: hjq
 * Date  : 2018/09/09 09:24
 * Name  : UiSeCursorDefault
 * Intro : Edit By hjq
 * Version : 1.0
 */
public class UiSeCursorDefault implements UiSeCursor {

    UiSelector mUiSelector;
    CursorType mCursorType;

    int mCursorWidth;
    int mCursorHeight;

    PopupWindow mCursorPopup;

    protected UiSeCursorDefault() {

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

    private void initialize() {
        float density = getUiSelector().getContext().getResources().getDisplayMetrics().density;
        mCursorWidth = mCursorHeight = (int) (density * 20 + 0.5f);

        mCursorPopup = new PopupWindow(getUiSelector().getContext());
        mCursorPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mCursorPopup.setClippingEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCursorPopup.setElevation(0.8f);
        }
        mCursorPopup.setContentView(new CursorView(getUiSelector().getContext()));
        mCursorPopup.setHeight(mCursorHeight);
        mCursorPopup.setWidth(mCursorWidth);

    }

    @Override
    public void setCursorType(CursorType type) {
        mCursorType = type;
    }

    @Override
    public CursorType getCursorType() {
        return mCursorType;
    }

    @Override
    public int getCursorWidth() {
        return mCursorWidth;
    }

    @Override
    public int getCursorHeight() {
        return mCursorHeight;
    }

    @Override
    public void showAtWinPoint(View view, float x, float y) {
        if (getCursorType() == CursorType.Left) {
            x -= getCursorWidth();
        }
        mCursorPopup.showAtLocation(view, Gravity.NO_GRAVITY, (int) x, (int) y);
    }

    @Override
    public void updateWinPoint(float x, float y) {
        if (getCursorType() == CursorType.Left) {
            x -= getCursorWidth();
        }
        mCursorPopup.update((int) x, (int) y, -1, -1);
    }

    @Override
    public void updateSize(int width, int height) {
        mCursorPopup.update(width, height);
    }

    @Override
    public boolean isShowing() {
        return mCursorPopup.isShowing();
    }

    @Override
    public void dismiss() {
        mCursorPopup.dismiss();
    }

    class CursorView extends View {

        Paint mPaint;

        public CursorView(Context context) {
            super(context);

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.BLUE);
        }

        int mOldStart;
        float[] mOldStartRawPoint;
        int mOldEnd;
        float[] mOldEndRawPoint;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    mOldStart = getUiSelector().getSelectionInfo().getStart();
                    mOldStartRawPoint = getUiSelector().getRawPointForSelectionOffset(mOldStart);
                    mOldEnd = getUiSelector().getSelectionInfo().getEnd();
                    mOldEndRawPoint = getUiSelector().getRawPointForSelectionOffset(mOldEnd);

                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:

                    getUiSelector().showUiMenu();

                    break;
                case MotionEvent.ACTION_MOVE:
                    getUiSelector().getUiResources().getUiMenu().dismiss();

                    float x = event.getRawX();
                    float y = event.getRawY();

                    if (getCursorType() == CursorType.Left) {

                        if (y > mOldEndRawPoint[1]){
                            y = mOldEndRawPoint[1];
                        }

                        getUiSelector().updateUiSelectionOffsetFromRawPoint(getCursorType(),
                                x,
                                y);

                    } else {

                        if (y < mOldStartRawPoint[1]){
                            y = mOldStartRawPoint[1];
                        }

                        getUiSelector().updateUiSelectionOffsetFromRawPoint(getCursorType(),
                                x,
                                y);

                    }

                    break;
            }

            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int radius = getCursorWidth() / 2;

            canvas.drawCircle(radius, radius, radius, mPaint);
            if (getCursorType() == CursorType.Left) {
                canvas.drawRect(radius, 0, getCursorWidth(), radius, mPaint);
            } else {
                canvas.drawRect(0, 0, radius, radius, mPaint);
            }
        }
    }
}
